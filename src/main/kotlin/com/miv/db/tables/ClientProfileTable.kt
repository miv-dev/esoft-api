package com.miv.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ClientProfileTable : IntIdTable("clients") {
    val user = reference("user_id", UserTable, ReferenceOption.CASCADE).uniqueIndex()
    val firstName = varchar("first_name", 50).nullable()
    val lastName = varchar("last_name", 50).nullable()
    val middleName = varchar("middle_name", 50).nullable()
    val phone = varchar("phone", 50).nullable()
    val email = varchar("email", 50).nullable()
}

