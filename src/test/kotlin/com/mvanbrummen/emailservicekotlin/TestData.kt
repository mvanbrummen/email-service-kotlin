package com.mvanbrummen.emailservicekotlin

import com.mvanbrummen.emailservicekotlin.api.EmailSendRequest
import com.mvanbrummen.emailservicekotlin.api.Person

class TestData {
    companion object {
        val emailSendRequest = EmailSendRequest(
            to = listOf(Person(email = "test123@gmail.com", name = "John Doe")),
            from = Person(email = "foobar@icloud.com", name = "Jane Doe"),
            cc = listOf(Person(email = "foobar@icloud.com", name = "Mr Test")),
            bcc = listOf(Person(email = "test@example.com", name = "Mr Test")),
            content = "Test Content",
            subject = "Test Subject"
        )

        val emailSendRequestInvalidEmails = EmailSendRequest(
            to = listOf(Person(email = "test123@gmail.com", name = "John Doe")),
            from = Person(email = "foobar@icloud.com", name = "Jane Doe"),
            cc = listOf(Person(email = "#@%^%#\$@#\$@#.com", name = "Mr Test")),
            bcc = listOf(Person(email = "@example.com", name = "Mr Test")),
            content = "Test Content",
            subject = "Test Subject"
        )
    }
}