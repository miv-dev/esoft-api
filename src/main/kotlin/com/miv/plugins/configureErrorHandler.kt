package com.miv.plugins

import com.miv.models.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.postgresql.util.PSQLException

fun Application.configureErrorHandler() {
    install(StatusPages){
        exception<Throwable> { call, throwable ->
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse(throwable.localizedMessage))
        }

        exception<PSQLException> { call, throwable ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(throwable.localizedMessage))
        }
    }
}
