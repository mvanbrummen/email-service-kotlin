package com.mvanbrummen.emailservicekotlin.api.gateway

import com.mvanbrummen.emailservicekotlin.api.EmailSendRequest
import com.mvanbrummen.emailservicekotlin.api.Person
import org.springframework.http.MediaType

data class SendGridEmailRequest(
    val from: Person,
    val personalizations: List<Personalizations>,
    val subject: String,
    val content: List<Content>,
)

fun fromEmailSendRequest(emailSendRequest: EmailSendRequest): SendGridEmailRequest {
    return SendGridEmailRequest(
        from = emailSendRequest.from,
        subject = emailSendRequest.subject,
        personalizations = listOf(
            Personalizations(
                to = emailSendRequest.to,
                cc = emailSendRequest.cc,
                bcc = emailSendRequest.bcc
            )
        ),
        content = listOf(
            Content(
                type = MediaType.TEXT_PLAIN_VALUE,
                value = emailSendRequest.content
            )
        )
    )
}

data class Personalizations(
    val to: List<Person>,
    val cc: List<Person>,
    val bcc: List<Person>,
)

data class Content(
    val type: String,
    val value: String
)