package com.mvanbrummen.emailservicekotlin.gateway

import com.mvanbrummen.emailservicekotlin.api.EmailSendRequest

interface EmailGateway {

    fun sendEmail(emailSendRequest: EmailSendRequest)
}

interface EmailGatewayChain {

    fun next(): EmailGateway?
}
