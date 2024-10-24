package com.miv.db.entities.real.state

import com.miv.db.entities.district.DistrictEntity
import com.miv.db.tables.real.state.RealStateDistrictTable
import com.miv.db.tables.real.state.RealStateTable
import com.miv.models.real.state.RealState
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class RealStateEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RealStateEntity>(RealStateTable)

    var type by RealStateTable.type
    var latitude by RealStateTable.latitude
    var longitude by RealStateTable.longitude
    var addressCity by RealStateTable.addressCity
    var addressHouse by RealStateTable.addressHouse
    var addressStreet by RealStateTable.addressStreet
    var addressNumber by RealStateTable.addressNumber
    var districts by DistrictEntity via RealStateDistrictTable

    fun toModel() = RealState(
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

