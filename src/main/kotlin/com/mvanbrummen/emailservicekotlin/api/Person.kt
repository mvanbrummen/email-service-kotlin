package com.mvanbrummen.emailservicekotlin.api

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

data class Person(
    @get:NotNull @get:Email val email: String? = null,
    val name: String? = null
)