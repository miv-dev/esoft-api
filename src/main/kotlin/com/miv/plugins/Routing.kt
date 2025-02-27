package com.miv.plugins

import com.miv.routes.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File


class AppRouting @AssistedInject constructor(
    private val userRoutingFactory: UserRouting.Factory,
    private val estateRoutingFactory: EstateRouting.Factory,
    private val offerRoutingFactory: OfferRouting.Factory,
    private val demandRoutingFactory: DemandRouting.Factory,
    private val dealRoutingFactory: DealRouting.Factory,
    private val eventRoutingFactory: EventRouting.Factory,
    @Assisted("application") private val application: Application,
) {
    fun configureRouting() {
        application.routing {
            userRoutingFactory.create(this).invoke()
            estateRoutingFactory.create(this).configureRouting()
            offerRoutingFactory.create(this).configureRouting()
            demandRoutingFactory.create(this).configureRouting()
            dealRoutingFactory.create(this).configureRouting()
            eventRoutingFactory.create(this).configureRouting()

            staticFiles("/media", File("media"))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("application") application: Application): AppRouting
    }

}