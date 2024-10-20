package com.miv.routes

import com.miv.dto.ClientDTO
import com.miv.dto.RealtorDTO
import com.miv.handlers.UserHandler
import com.miv.models.Role
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.util.getOrFail

class UserRouting @AssistedInject constructor(
    private val handler: UserHandler,
    @Assisted("route") route: Route,
) : Route by route {

    operator fun invoke() {
        route("/users") {
            get("/search") {
                val query = call.request.queryParameters.getOrFail("query")
                val role = call.request.queryParameters.getOrFail<Role>("role")

                val profiles = handler.search(query, role)
                call.respond(profiles)
            }

            route("/realtor") {
                post {
                    val data = call.receive<RealtorDTO>()
                    handler.createRealtor(data).also {
                        call.respond(it)
                    }
                }
                put("/{id}") {
                    val id = call.parameters.getOrFail<String>("id")
                    val data = call.receive<RealtorDTO>()
                    handler.updateRealtor(data, id).also {
                        call.respond(it)
                    }

                }
            }

            route("/client") {
                post {
                    val data = call.receive<ClientDTO>()
                    handler.createClient(data).also {
                        call.respond(it)
                    }
                }
                put("/{id}") {
                    val id = call.parameters.getOrFail<String>("id")
                    val data = call.receive<ClientDTO>()
                    handler.updateClient(data, id).also {
                        call.respond(it)
                    }
                }
            }

            delete("/{id}") {
                val id = call.parameters.getOrFail<String>("id")
                handler.deleteUser(id).also {
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("route") route: Route,
        ): UserRouting
    }
}