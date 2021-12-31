package com.mvanbrummen.emailservicekotlin.configuration

import com.mvanbrummen.emailservicekotlin.gateway.EmailGateway
import com.mvanbrummen.emailservicekotlin.gateway.MailGunEmailGateway
import com.mvanbrummen.emailservicekotlin.gateway.SendGridEmailGateway
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmailGatewayConfiguration {

    @Bean
    fun emailGatewayChain(@Value("\${email.gateway.sendgrid.base-url}") sendGridApiUrl: String,
                     @Value("\${email.gateway.mailgun.base-url}") mailgunApiUrl: String,
                     ): EmailGateway {
        val sendGridEmailGateway = SendGridEmailGateway(null, sendGridApiUrl)
        val mailgunEmailGateway = MailGunEmailGateway(sendGridEmailGateway, mailgunApiUrl)

        return mailgunEmailGateway
    }
}