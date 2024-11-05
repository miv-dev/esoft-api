package com.miv.routes

import com.miv.dto.EventDTO
import com.miv.handlers.EventHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

class EventRouting @AssistedInject constructor(
    private val handler: EventHandler,
    @Assisted("route") route: Route,
) : Route by route {
    fun configureRouting() {
        route("/events") {
            get {
                handler.getAll().also {
                    call.respond(it)
                }
            }

            get("/{id}") {
                val id = call.parameters.getOrFail<String>("id")

                handler.getOne(id).also {
                    call.respond(it)
                }
            }

            post {
                val data = call.receive<EventDTO>()

                handler.create(data).also {
                    call.respond(it)
                }
            }
            put("/{id}") {
                val id = call.parameters.getOrFail<String>("id")
                val data = call.receive<EventDTO>()
                handler.update(data, id).also {
                    call.respond(it)
                }
            }
            delete("/{id}") {
                val id = call.parameters.getOrFail<String>("id")
                handler.delete(id).also {
                    call.respond(OK)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("route") route: Route,
        ): EventRouting
    }
}