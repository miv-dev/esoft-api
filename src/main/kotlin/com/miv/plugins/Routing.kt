package com.miv.plugins

import com.miv.routes.RealStateRouting
import com.miv.routes.UserRouting
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.server.application.*
import io.ktor.server.routing.*


class AppRouting @AssistedInject constructor(
    private val userRoutingFactory: UserRouting.Factory,
    private val realStateRoutingFactory: RealStateRouting.Factory,
    @Assisted("application") private val application: Application,
) {
    fun configureRouting() {
        application.routing {
            userRoutingFactory.create(this).invoke()
            realStateRoutingFactory.create(this).configureRouting()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("application") application: Application): AppRouting
    }

}