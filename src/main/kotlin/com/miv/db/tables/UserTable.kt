package com.miv.db.tables

import com.miv.models.user.Role
import org.jetbrains.exposed.dao.id.UUIDTable

object UserTable : UUIDTable("users", "id") {
    val role = enumeration("role", Role::class).default(Role.CLIENT)
    val avatar = varchar("avatar", 255).nullable()
}