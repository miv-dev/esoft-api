package com.miv.db.tables


import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import java.util.UUID

object ClientProfileTable : IdTable<UUID>("clients") {
    val firstName = varchar("first_name", 50).nullable()
    val lastName = varchar("last_name", 50).nullable()
    val middleName = varchar("middle_name", 50).nullable()
    val phone = varchar("phone", 50).nullable()
    val email = varchar("email", 50).nullable()

    override val id: Column<EntityID<UUID>> = reference("user_id", UserTable, ReferenceOption.CASCADE)
}

