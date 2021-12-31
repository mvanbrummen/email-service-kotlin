package com.mvanbrummen.emailservicekotlin

import com.mvanbrummen.emailservicekotlin.api.EmailSendRequest
import com.mvanbrummen.emailservicekotlin.api.Person

class TestData {
    companion object {
        val emailSendRequest = EmailSendRequest(
            to = listOf(Person(email = "test123@gmail.com")),
            from = Person(email = "foobar@icloud.com"),
            cc = listOf(),
            bcc = listOf(),
            content = "Test Content",
            subject = "Test Subject"
        )

        val emailSendRequestInvalidEmails = EmailSendRequest(
            to = listOf(Person(email = "plainaddress", name = "John Doe")),
            from = Person(email = "#@%^%#\$@#\$@#.com", name = "Jane Doe"),
            cc = listOf(Person(email = "@example.com", name = "Mr Test")),
            bcc = listOf(Person(email = "@example.com (Joe Smith)", name = "Mr Test")),
            content = "Test Content",
            subject = "Test Subject"
        )
    }
}