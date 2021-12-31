package com.mvanbrummen.emailservicekotlin.gateway

import com.mvanbrummen.emailservicekotlin.api.EmailSendRequest
import com.mvanbrummen.emailservicekotlin.api.Person
import com.mvanbrummen.emailservicekotlin.exception.EmailGatewayDownException
import khttp.post
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap

class MailGunEmailGateway(
    private val nextGateway: EmailGateway?,
    private val apiBaseUrl: String
) : EmailGateway, EmailGatewayChain {
    override fun sendEmail(emailSendRequest: EmailSendRequest) {
        val form = buildForm(emailSendRequest)

        try {
            val response = post("$apiBaseUrl/messages", data = form)
            if (response.statusCode != HttpStatus.OK.value()) {
                handleError("Email gateway returned ${response.statusCode}", emailSendRequest)
            }
        } catch (ex: Exception) {
            handleError(ex.message ?: "Gateway error occurred", emailSendRequest)
        }
    }

    private fun buildForm(emailSendRequest: EmailSendRequest): LinkedMultiValueMap<String, String> {
        val form = LinkedMultiValueMap<String, String>()
        form["to"] = (emailSendRequest.to ?: listOf()).map(Person::email)
        form["from"] = listOf(emailSendRequest.from?.email)
        form["subject"] = listOf(emailSendRequest.subject)
        form["text"] = listOf(emailSendRequest.content)
        form["cc"] = emailSendRequest.cc.map(Person::email)
        form["bcc"] = emailSendRequest.bcc.map(Person::email)

        return form
    }

    private fun handleError(message: String, emailSendRequest: EmailSendRequest) {
        if (next() == null) {
            throw EmailGatewayDownException(message)
        } else {
            next()?.sendEmail(emailSendRequest)
        }
    }

    override fun next(): EmailGateway? = nextGateway
}