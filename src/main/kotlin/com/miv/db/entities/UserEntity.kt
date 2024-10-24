package com.miv.db.entities

import com.miv.db.tables.ClientProfileTable.enumeration
import com.miv.db.tables.UserTable
import com.miv.models.AdministratorProfile
import com.miv.models.Profile
import com.miv.models.Role
import com.miv.models.User
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UserTable)

    var role by UserTable.role

    fun toModel() = User(
        id = id.value,
        role = role
    )

}