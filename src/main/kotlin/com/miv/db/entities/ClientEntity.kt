package com.miv.db.entities

import com.miv.db.tables.ClientProfileTable
import com.miv.models.ClientProfile
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ClientEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClientEntity>(ClientProfileTable)

    var firstName by ClientProfileTable.firstName
    var lastName by ClientProfileTable.lastName
    var middleName by ClientProfileTable.middleName
    var user by UserEntity referencedOn ClientProfileTable.user
    var phone by ClientProfileTable.phone
    var email by ClientProfileTable.email

    fun toModel() = ClientProfile(
        id.value,
        user = user.toModel(),
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        phone = phone,
        email = email
    )

}