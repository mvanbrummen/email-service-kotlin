package com.mvanbrummen.emailservicekotlin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mvanbrummen.emailservicekotlin.TestData
import com.mvanbrummen.emailservicekotlin.api.EmailSendRequest
import com.mvanbrummen.emailservicekotlin.api.Person
import com.mvanbrummen.emailservicekotlin.exception.EmailGatewayDownException
import com.mvanbrummen.emailservicekotlin.service.EmailService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [EmailController::class])
internal class EmailControllerIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    @MockBean
    lateinit var emailService: EmailService

    @Test
    fun `should return 202 accepted when valid request`() {
        val request = TestData.emailSendRequest

        mockMvc.perform(
            post("/email/send")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isAccepted)
    }

    @Test
    fun `should call business logic when valid request`() {
        val request = TestData.emailSendRequest

        mockMvc.perform(
            post("/email/send")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isAccepted)

        verify(emailService).sendEmail(request)
    }

    @Test
    fun `should return 400 error when mandatory fields are missing`() {
        val request = EmailSendRequest()

        mockMvc.perform(
            post("/email/send")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(
                content().json(
                    """
                        {
                          "subject": "must not be null",
                          "from": "must not be null",
                          "to": "must not be empty",
                          "content": "must not be null"
                        }
                        """
                )
            )
    }

    @Test
    fun `should return 400 error when email validation fails`() {
        val request = TestData.emailSendRequestInvalidEmails

        mockMvc.perform(
            post("/email/send")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest)
            .andExpect(
                content().json(
                    """
                        {
                          "to[0].email": "must be a well-formed email address",
                          "bcc[0].email": "must be a well-formed email address",
                          "cc[0].email": "must be a well-formed email address",
                          "from.email": "must be a well-formed email address"
                        }
                        """
                )
            )
    }

    @Test
    fun `should return 502 error when email gateway fails`() {
        val request = TestData.emailSendRequest

        doThrow(EmailGatewayDownException("Gateway down")).`when`(emailService).sendEmail(request)

        mockMvc.perform(
            post("/email/send")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadGateway)
            .andExpect(
                content().json(
                    """
                        {
                          "error": "Service is currently unavailable. Please try again later."
                        }
                        """
                )
            )
    }
}