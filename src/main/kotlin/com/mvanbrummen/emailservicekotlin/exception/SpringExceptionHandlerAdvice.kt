package com.mvanbrummen.emailservicekotlin.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SpringExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): Map<String, String> {
        val errors = HashMap<String, String>()
        ex.bindingResult.allErrors.map {
            val fieldName = it.objectName
            val errorMessage = it.defaultMessage

            errors.put(fieldName, errorMessage ?: "")
        }

        return errors
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler
    fun handleEmailGatewayDownException(ex: EmailGatewayDownException): Map<String, String> {
        return mapOf("error" to "Service is currently unavailable. Please try again later.")
    }
}