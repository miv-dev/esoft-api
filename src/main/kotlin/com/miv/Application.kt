package com.miv

import com.miv.db.DatabaseConfig
import com.miv.db.DatabaseManager
import com.miv.di.DaggerApplicationComponent
import com.miv.plugins.*
import com.miv.utils.stringProperty
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }
    configureErrorHandler()
    cors()
    val app = DaggerApplicationComponent.create()


    val dbConfig = loadDatabaseConfig()
    val db = DatabaseManager(dbConfig)
    db.connect()

    val routing = app.routingFactory()
    routing.create(this).configureRouting()
}


fun Application.loadDatabaseConfig(): DatabaseConfig {
    return DatabaseConfig(
        stringProperty("db.url"),
        stringProperty("db.username"),
        stringProperty("db.password"),
    )
}