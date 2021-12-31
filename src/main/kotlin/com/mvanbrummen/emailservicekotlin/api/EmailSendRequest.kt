package com.mvanbrummen.emailservicekotlin.api

import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class EmailSendRequest(
    @Valid val from: Person,

    val subject: String,
    val content: String,

    @NotEmpty @Valid val to: List<Person>,
    @Valid val cc: List<Person>,
    @Valid val bcc: List<Person>,

    )