package com.mvanbrummen.emailservicekotlin.service

import com.mvanbrummen.emailservicekotlin.gateway.EmailGateway
import org.springframework.stereotype.Service

@Service
class EmailService(private val emailGateway: EmailGateway) : EmailGateway by emailGateway
