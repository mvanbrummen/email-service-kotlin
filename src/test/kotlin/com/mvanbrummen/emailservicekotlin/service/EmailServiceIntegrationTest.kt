package com.mvanbrummen.emailservicekotlin.service

import com.mvanbrummen.emailservicekotlin.TestData
import com.mvanbrummen.emailservicekotlin.exception.EmailGatewayDownException
import io.specto.hoverfly.junit.core.Hoverfly
import io.specto.hoverfly.junit.core.SimulationSource.dsl
import io.specto.hoverfly.junit.dsl.HoverflyDsl
import io.specto.hoverfly.junit.dsl.HoverflyDsl.service
import io.specto.hoverfly.junit.dsl.ResponseCreators.serverError
import io.specto.hoverfly.junit.dsl.ResponseCreators.success
import io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.equalsToJson
import io.specto.hoverfly.junit5.HoverflyExtension
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.matches
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(HoverflyExtension::class)
internal class EmailServiceIntegrationTest @Autowired constructor(
    val emailService: EmailService
) {

    @Test
    fun `should send email to primary provider when request is success`(hoverfly: Hoverfly) {
        val request = TestData.emailSendRequest

        hoverfly.simulate(
            dsl(
                service("api.mailgun.net")
                    .post("/v3/sandbox3793fb4e82f6408281f7901e8578d9fc.mailgun.org/messages")
                    .body("to=%5Btest123%40gmail.com%5D\u0026from=%5Bfoobar%40icloud.com%5D\u0026subject=%5BTest+Subject%5D\u0026text=%5BTest+Content%5D\u0026cc=%5B%5D\u0026bcc=%5B%5D")
                    .willReturn(
                        success(
                            """
                                {
                                  "id": "<20211112053919.32086b1dc8d5e76e@sandbox3793fb4e82f6408281f7901e8578d9fc.mailgun.org>",
                                  "message": "Queued. Thank you."
                                }
                                """, MediaType.APPLICATION_JSON_VALUE
                        )
                    )
            )
        );

        emailService.sendEmail(request);

        hoverfly.verifyZeroRequestTo(service(matches("api.sendgrid.com")));
    }

    @Test
    fun `should send email to fallback provider when request fails`(hoverfly: Hoverfly) {
        val request = TestData.emailSendRequest

        hoverfly.simulate(
            dsl(
                service("api.mailgun.net")
                    .post("/v3/sandbox3793fb4e82f6408281f7901e8578d9fc.mailgun.org/messages")
                    .anyBody()
                    .willReturn(serverError()),
                service("api.sendgrid.com")
                    .post("/v3/mail/send")
                    .body(
                        equalsToJson(
                            """
                                {
                                  "from": {
                                    "email": "foobar@icloud.com"
                                  },
                                  "personalizations": [
                                    {
                                      "to": [
                                        {
                                          "email": "test123@gmail.com"
                                        }
                                      ],
                                      "cc": [],
                                      "bcc": []
                                    }
                                  ],
                                  "subject": "Test Subject",
                                  "content": [
                                    {
                                      "type": "text/plain",
                                      "value": "Test Content"
                                    }
                                  ]
                                }
                                    """
                        )
                    )
                    .willReturn(HoverflyDsl.response().status(202))
            )
        )

        emailService.sendEmail(request)

        hoverfly.verifyAll()
    }

    @Test
    fun `should throw email gateway down exception when both gateway calls fail`(hoverfly: Hoverfly) {
        val request = TestData.emailSendRequest

        hoverfly.simulate(
            dsl(
                service("api.mailgun.net")
                    .post("/v3/sandbox3793fb4e82f6408281f7901e8578d9fc.mailgun.org/messages")
                    .anyBody()
                    .willReturn(serverError()),
                service("api.sendgrid.com")
                    .post("/v3/mail/send")
                    .anyBody()
                    .willReturn(serverError())
            )
        )

        assertThatThrownBy { emailService.sendEmail(request) }
            .isInstanceOf(EmailGatewayDownException::class.java)

        hoverfly.verifyAll()
    }
}