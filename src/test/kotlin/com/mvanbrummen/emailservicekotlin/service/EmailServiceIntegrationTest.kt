package com.mvanbrummen.emailservicekotlin.service

import com.mvanbrummen.emailservicekotlin.TestData
import io.specto.hoverfly.junit.core.Hoverfly
import io.specto.hoverfly.junit.core.SimulationSource.dsl
import io.specto.hoverfly.junit.dsl.HoverflyDsl.service
import io.specto.hoverfly.junit.dsl.ResponseCreators.success
import io.specto.hoverfly.junit5.HoverflyExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.matches
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("Test")
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
                    .body("to=michaelvanbrummen%40gmail.com&from=michaelvanbrummen%40icloud.com&subject=Test+Subject&text=Test+Content")
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

    }

    @Test
    fun `should throw email gateway down exception when both gateway calls fail`(hoverfly: Hoverfly) {

    }
}