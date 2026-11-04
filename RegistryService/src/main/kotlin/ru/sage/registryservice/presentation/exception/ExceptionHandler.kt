package ru.sage.registryservice.presentation.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.sage.registryservice.domain.exception.CustomExceptions.PluginInstanceNotFoundException
import ru.sage.registryservice.domain.exception.CustomExceptions.PluginInstancesNotFoundException
import ru.sage.registryservice.domain.exception.CustomExceptions.PluginNotFoundException
import ru.sage.registryservice.domain.exception.CustomExceptions.PluginCapabilitiesNotFoundException

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(PluginNotFoundException::class)
    fun handlePluginNotFoundException(e: PluginNotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            e.message ?: "Plugin not found"
        )

    @ExceptionHandler(PluginInstancesNotFoundException::class)
    fun handlePluginInstancesNotFoundException(e: PluginInstancesNotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            e.message ?: "Plugin instances not found"
        )

    @ExceptionHandler(PluginInstanceNotFoundException::class)
    fun handlePluginInstanceNotFoundException(e: PluginInstanceNotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            e.message ?: "Plugin instance not found"
        )

    @ExceptionHandler(PluginCapabilitiesNotFoundException::class)
    fun handlePluginInstanceNotFoundException(e: PluginCapabilitiesNotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            e.message ?: "Plugin capabilities not found"
        )

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ProblemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            e.message ?: "Unknown error"
        )
}