package com.miv.db.tables

import com.miv.models.Role
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table

object UserTable : UUIDTable("users", "id") {
    val role = enumeration("role", Role::class).default(Role.CLIENT)
}