package com.miv.db.entities

import com.miv.db.tables.ClientProfileTable
import com.miv.models.ClientProfile
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class ClientEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<ClientEntity>(ClientProfileTable)

    var firstName by ClientProfileTable.firstName
    var lastName by ClientProfileTable.lastName
    var middleName by ClientProfileTable.middleName
    var user by UserEntity referencedOn ClientProfileTable.user
    var phone by ClientProfileTable.phone
    var email by ClientProfileTable.email

    fun toModel() = ClientProfile(
        user = user.toModel(),
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        phone = phone,
        email = email
    )

}