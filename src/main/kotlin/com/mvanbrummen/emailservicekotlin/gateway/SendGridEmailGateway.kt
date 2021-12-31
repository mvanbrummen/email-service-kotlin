package com.mvanbrummen.emailservicekotlin.gateway

import com.mvanbrummen.emailservicekotlin.api.EmailSendRequest
import com.mvanbrummen.emailservicekotlin.api.gateway.fromEmailSendRequest
import com.mvanbrummen.emailservicekotlin.exception.EmailGatewayDownException
import khttp.post
import org.json.JSONObject
import org.springframework.http.HttpStatus

class SendGridEmailGateway(
    private val nextGateway: EmailGateway?,
    private val apiBaseUrl: String
) : EmailGateway, EmailGatewayChain {

    override fun sendEmail(emailSendRequest: EmailSendRequest) {
        try {
            val resp = post("$apiBaseUrl/mail/send", data = JSONObject(fromEmailSendRequest(emailSendRequest)))
            if (resp.statusCode != HttpStatus.NO_CONTENT.value()) {
                handleError("Email gateway returned ${resp.statusCode}", emailSendRequest)
            }
        } catch (ex: Exception) {
            handleError(ex.message ?: "Gateway error occurred", emailSendRequest)
        }
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