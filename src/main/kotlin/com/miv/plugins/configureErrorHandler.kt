package com.miv.plugins

import com.miv.models.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Application.configureErrorHandler() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is ExposedSQLException -> {
                    if (throwable.sqlState == "23503") {
                        call.respond(HttpStatusCode.Conflict)
                    } else call.respond(HttpStatusCode.BadRequest, ErrorResponse(throwable.localizedMessage))
                }

                else -> call.respond(HttpStatusCode.InternalServerError, ErrorResponse(throwable.localizedMessage))
            }
        }
    }
}
