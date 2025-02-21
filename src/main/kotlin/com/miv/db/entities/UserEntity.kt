package com.miv.db.entities

import com.miv.db.tables.UserTable
import com.miv.models.user.User
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UserTable)

    var role by UserTable.role
    var avatar by UserTable.avatar

    fun toModel() = User(
        id = id.value,
        role = role,
        avatar = avatar
    )

}