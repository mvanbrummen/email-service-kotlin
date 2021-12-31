package com.mvanbrummen.emailservicekotlin.api

import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class EmailSendRequest(
    @get:NotNull @get:Valid val from: Person? = null,

    @get:NotNull val subject: String? = null,
    @get:NotNull val content: String? = null,

    @get:NotEmpty @get:Valid val to: List<Person>? = null,
    @get:Valid val cc: List<Person> = emptyList(),
    @get:Valid val bcc: List<Person> = emptyList(),

    )