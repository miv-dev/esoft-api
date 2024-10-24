package com.miv.db.entities

import com.miv.db.tables.ClientProfileTable
import com.miv.db.tables.RealtorProfileTable
import com.miv.models.ClientProfile
import com.miv.models.RealtorProfile
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

class RealtorEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RealtorEntity>(RealtorProfileTable)

    var firstName by RealtorProfileTable.firstName
    var lastName by RealtorProfileTable.lastName
    var middleName by RealtorProfileTable.middleName
    var user by UserEntity referencedOn RealtorProfileTable.id
    var dealShare by RealtorProfileTable.dealShare

    fun toModel() = RealtorProfile(
        user = user.toModel(),
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        dealShare = dealShare,
    )

}