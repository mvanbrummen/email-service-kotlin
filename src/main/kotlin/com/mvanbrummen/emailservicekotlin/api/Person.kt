package com.mvanbrummen.emailservicekotlin.api

import javax.validation.constraints.Email

data class Person(
    @Email val email: String,
    val name: String
)