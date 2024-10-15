package com.miv

import com.miv.di.DaggerApplicationComponent
import com.miv.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val routing = DaggerApplicationComponent.create().routingFactory()

    routing.create(this)
}
