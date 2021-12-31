package com.mvanbrummen.emailservicekotlin.api

import javax.validation.Valid
import javax.validation.constraints.NotEmpty

data class EmailSendRequest(
    @Valid val from: Person,

    val subject: String,
    val content: String,

    @NotEmpty @Valid val to: List<Person>,
    @Valid val cc: List<Person>,
    @Valid val bcc: List<Person>,

    )