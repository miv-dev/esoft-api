package com.miv.db.entities.estate

import com.miv.db.entities.district.DistrictEntity
import com.miv.db.tables.estate.RealStateDistrictTable
import com.miv.db.tables.estate.EstateTable
import com.miv.models.estate.Estate
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class EstateEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EstateEntity>(EstateTable)

    var type by EstateTable.type
    var latitude by EstateTable.latitude
    var longitude by EstateTable.longitude
    var addressCity by EstateTable.addressCity
    var addressHouse by EstateTable.addressHouse
    var addressStreet by EstateTable.addressStreet
    var addressNumber by EstateTable.addressNumber
    var districts by DistrictEntity via RealStateDistrictTable

    fun toModel() = Estate(
        id = id.value,
        type = type,
        latitude = latitude,
        longitude = longitude,
        addressCity = addressCity,
        addressStreet = addressStreet,
        addressHouse = addressHouse,
        addressNumber = addressNumber,
        districts = districts.map { it.toModel() }
    )
}

