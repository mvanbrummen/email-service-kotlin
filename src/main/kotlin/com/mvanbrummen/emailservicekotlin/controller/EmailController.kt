package com.mvanbrummen.emailservicekotlin.controller

import com.mvanbrummen.emailservicekotlin.api.EmailSendRequest
import com.mvanbrummen.emailservicekotlin.service.EmailService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class EmailController(private val emailService: EmailService) {

    @PostMapping("/email/send")
    fun sendEmail(@Valid @RequestBody emailSendRequest: EmailSendRequest): ResponseEntity<Void> {
        emailService.sendEmail(emailSendRequest)

        return ResponseEntity.accepted().build()
    }
}