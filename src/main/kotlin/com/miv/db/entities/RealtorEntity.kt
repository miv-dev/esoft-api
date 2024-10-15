package com.miv.db.entities

import com.miv.db.tables.ClientProfileTable
import com.miv.db.tables.RealtorProfileTable
import com.miv.models.ClientProfile
import com.miv.models.RealtorProfile
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RealtorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RealtorEntity>(RealtorProfileTable)

    var firstName by RealtorProfileTable.firstName
    var lastName by RealtorProfileTable.lastName
    var middleName by RealtorProfileTable.middleName
    var user by UserEntity referencedOn RealtorProfileTable.user
    var feePercentage by RealtorProfileTable.feePercentage

    fun toModel() = RealtorProfile(
        id.value,
        user = user.toModel(),
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        feePercentage = feePercentage,
    )

}