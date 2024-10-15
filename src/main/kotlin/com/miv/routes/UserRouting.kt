package com.miv.routes

import com.miv.handlers.UserHandler
import com.miv.models.Role
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
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
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("route") route: Route,
        ): UserRouting
    }
}