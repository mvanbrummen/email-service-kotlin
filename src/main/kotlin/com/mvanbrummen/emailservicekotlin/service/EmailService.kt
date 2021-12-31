package com.mvanbrummen.emailservicekotlin.service

import com.mvanbrummen.emailservicekotlin.api.EmailSendRequest
import com.mvanbrummen.emailservicekotlin.gateway.EmailGateway
import org.springframework.stereotype.Service

@Service
class EmailService(private val emailGateway: EmailGateway) {

    fun sendEmail(emailSendRequest: EmailSendRequest) = emailGateway.sendEmail(emailSendRequest)
}