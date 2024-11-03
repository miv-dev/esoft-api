package com.miv.routes

import com.miv.dto.DealDTO
import com.miv.dto.DemandOffersType
import com.miv.handlers.DealHandler
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

class DealRouting @AssistedInject constructor(
    private val handler: DealHandler,
    private val offerHandler: OfferHandler,
    private val demandHandler: DemandHandler,
    @Assisted("route") route: Route,
) : Route by route {

    fun configureRouting() {
        route("/deals") {
            get {
                handler.getAll().also {
                    call.respond(it)
                }
            }

            get("/filters") {
                val inSummary = call.request.queryParameters["in-summary"].toBoolean()

                handler.getFilters(inSummary).also { call.respond(it) }
            }

            post {
                val deal = call.receive<DealDTO>()

                handler.create(deal).also {
                    call.respond(it)
                }
            }

            put("/{id}") {
                val id = call.parameters.getOrFail<String>("id")
                val data = call.receive<DealDTO>()
                handler.update(id, data).also {
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
        ): DealRouting
    }
}