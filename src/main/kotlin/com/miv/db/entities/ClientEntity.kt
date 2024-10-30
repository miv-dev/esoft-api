package com.miv.db.entities

import com.miv.db.tables.ClientProfileTable
import com.miv.models.user.ClientProfile
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class ClientEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ClientEntity>(ClientProfileTable)

    var firstName by ClientProfileTable.firstName
    var lastName by ClientProfileTable.lastName
    var middleName by ClientProfileTable.middleName
    var user by UserEntity referencedOn ClientProfileTable.id
    var phone by ClientProfileTable.phone
    var email by ClientProfileTable.email

    fun toModel() = ClientProfile(
        id = id.value,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        phone = phone,
        email = email
    )

}