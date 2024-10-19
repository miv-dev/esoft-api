package com.miv.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import kotlin.also

class DatabaseManager(private val config: DatabaseConfig) {

    private fun migrate() {

        val flyway = Flyway
            .configure()
            .dataSource(config.url, config.username, config.password)
            .load()
        flyway.migrate()
    }

    fun connect() {
        val config = HikariConfig().also { hikari ->
            hikari.driverClassName = DRIVER_CLASS_NAME
            hikari.transactionIsolation = TRANSACTION_ISOLATION
            hikari.jdbcUrl = config.url
            hikari.username = config.username
            hikari.password = config.password
        }

        migrate()

        Database.Companion.connect(HikariDataSource(config))

    }

    companion object {
        const val DRIVER_CLASS_NAME = "org.postgresql.Driver"
        const val TRANSACTION_ISOLATION = "TRANSACTION_REPEATABLE_READ"
    }
}

