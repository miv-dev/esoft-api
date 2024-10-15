package com.miv.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ClientProfileTable : IntIdTable() {
    val user = reference("user", UserTable, ReferenceOption.CASCADE).uniqueIndex()
    val firstName = varchar("first_name", 50).nullable()
    val lastName = varchar("last_name", 50).nullable()
    val middleName = varchar("middle_name", 100).nullable()
    val phone = varchar("phone", 100).nullable().uniqueIndex()
    val email = varchar("email", 100).nullable().uniqueIndex()
}

