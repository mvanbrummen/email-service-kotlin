package com.mvanbrummen.emailservicekotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmailServiceKotlinApplication

fun main(args: Array<String>) {
	runApplication<EmailServiceKotlinApplication>(*args)
}
