package com.miv.plugins

import com.miv.routes.DemandRouting
import com.miv.routes.OfferRouting
import com.miv.routes.EstateRouting
import com.miv.routes.UserRouting
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.server.application.*
import io.ktor.server.routing.*


class AppRouting @AssistedInject constructor(
    private val userRoutingFactory: UserRouting.Factory,
    private val estateRoutingFactory: EstateRouting.Factory,
    private val offerRoutingFactory: OfferRouting.Factory,
    private val demandRoutingFactory: DemandRouting.Factory,
    @Assisted("application") private val application: Application,
) {
    fun configureRouting() {
        application.routing {
            userRoutingFactory.create(this).invoke()
            estateRoutingFactory.create(this).configureRouting()
            offerRoutingFactory.create(this).configureRouting()
            demandRoutingFactory.create(this).configureRouting()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("application") application: Application): AppRouting
    }

}