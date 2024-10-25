package com.miv.routes

import com.miv.dto.DemandDTO
import com.miv.dto.OfferDTO
import com.miv.handlers.DemandHandler
import com.miv.handlers.OfferHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

class DemandRouting @AssistedInject constructor(
    private val handler: DemandHandler,
    @Assisted("route") route: Route,
) : Route by route {

    fun configureRouting() {
        route("/demands") {
            get {
                val userID = call.parameters["user-id"]
                val result = if (userID == null) {
                    handler.get()
                } else {
                    handler.get(userID)
                }
                result.also {
                    call.respond(it)
                }
            }

            get("/{id}") {
                val id = call.parameters.getOrFail<String>("id")
                handler.getByID(id).also {
                    call.respond(it)
                }
            }

            post {
                val data = call.receive<DemandDTO>()
                handler.create(data).also {
                    call.respond(it)
                }
            }

            put("/{id}") {
                val id = call.parameters.getOrFail<String>("id")
                val data = call.receive<DemandDTO>()
                handler.update(data, id).also {
                    call.respond(it)
                }
            }
            delete("/{id}") {
                val id = call.parameters.getOrFail<String>("id")
                handler.delete(id).also {
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("route") route: Route,
        ): DemandRouting
    }
}