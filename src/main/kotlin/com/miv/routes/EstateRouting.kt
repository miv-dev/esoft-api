package com.miv.routes

import com.miv.dto.RealStateClassDTO
import com.miv.handlers.EstateHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

class EstateRouting @AssistedInject constructor(
    private val handler: EstateHandler,
    @Assisted("route") route: Route,
) : Route by route {

    fun configureRouting() {
        route("/real-state") {
            get("/filters") {
                handler.getFilters().also {
                    call.respond(it)
                }
            }

            get("/{id}") {
                val id = call.parameters.getOrFail<String>("id")
                handler.get(id).also {
                    call.respond(it)
                }
            }

            get("/search") {
                val query = call.request.queryParameters["query"]
                val type = call.request.queryParameters["type"]
                val district = call.request.queryParameters["district"]
                val sortBy = call.request.queryParameters["sort-by"]
                val sortDirection = call.request.queryParameters["sort-direction"]
                handler.search(query, type, district, sortBy, sortDirection).also {
                    call.respond(it)
                }
            }
            post {
                val data = call.receive<RealStateClassDTO>()
                handler.create(data).also {
                    call.respond(it)
                }
            }
            put("/{id}") {
                val id = call.parameters.getOrFail<String>("id")
                val data = call.receive<RealStateClassDTO>()
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
        ): EstateRouting
    }
}