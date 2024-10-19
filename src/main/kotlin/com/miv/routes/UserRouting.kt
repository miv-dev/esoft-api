package com.miv.routes

import com.miv.dto.ClientPost
import com.miv.dto.RealtorPost
import com.miv.handlers.UserHandler
import com.miv.models.Role
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.util.getOrFail
import java.util.*

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

            post("/realtor"){
                val data = call.receive<RealtorPost>()
                handler.createRealtor(data).also {
                    call.respond(it)
                }
            }

            post("/client") {
                val data = call.receive<ClientPost>()
                handler.createClient(data).also {
                    call.respond(it)
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