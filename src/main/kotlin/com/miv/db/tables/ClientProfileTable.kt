package com.miv.db.tables

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ClientProfileTable : CompositeIdTable("clients") {
    val user = reference("user_id", UserTable, ReferenceOption.CASCADE)
    val firstName = varchar("first_name", 50).nullable()
    val lastName = varchar("last_name", 50).nullable()
    val middleName = varchar("middle_name", 50).nullable()
    val phone = varchar("phone", 50).nullable()
    val email = varchar("email", 50).nullable()

    init {
        addIdColumn(user)
    }

    override val primaryKey = PrimaryKey(user)
}

