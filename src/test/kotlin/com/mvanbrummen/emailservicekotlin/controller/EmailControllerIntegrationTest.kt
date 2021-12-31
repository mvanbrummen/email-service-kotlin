package com.mvanbrummen.emailservicekotlin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mvanbrummen.emailservicekotlin.TestData
import com.mvanbrummen.emailservicekotlin.service.EmailService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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

    }

    @Test
    fun `should return 400 error when mandatory fields are missing`() {

    }

    @Test
    fun `should return 400 error when email validation fails`() {

    }

    @Test
    fun `should return 502 error when email gateway fails`() {

    }

}