package com.miv.routes

import com.miv.dto.OfferDTO
import com.miv.handlers.OfferHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

class OfferRouting @AssistedInject constructor(
    private val handler: OfferHandler,
    @Assisted("route") route: Route,
) : Route by route {

    fun configureRouting() {
        route("/offers") {
            get {
                val userID = call.request.queryParameters["user-id"]
                val demandID = call.request.queryParameters["demand-id"]
                val withoutDeals = call.request.queryParameters["without-deals"]
                val inSummary = call.request.queryParameters["in-summary"]
                val result = handler.get(userID, demandID, withoutDeals.toBoolean(), inSummary.toBoolean())
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
                val data = call.receive<OfferDTO>()
                handler.create(data).also {
                    call.respond(it)
                }
            }

            put("/{id}") {
                val id = call.parameters.getOrFail<String>("id")
                val data = call.receive<OfferDTO>()
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
        ): OfferRouting
    }
}