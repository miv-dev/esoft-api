package com.miv.db.entities.real.state

import com.miv.db.entities.district.DistrictEntity
import com.miv.db.tables.real.state.RealStateDistrictTable
import com.miv.db.tables.real.state.RealStateTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class RealStateEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RealStateEntity>(RealStateTable)

    val type by RealStateTable.type
    val latitude by RealStateTable.latitude
    val longitude by RealStateTable.longitude
    val addressCity by RealStateTable.addressCity
    val addressHouse by RealStateTable.addressHouse
    val addressStreet by RealStateTable.addressStreet
    val addressNumber by RealStateTable.addressNumber
    val districts by DistrictEntity via RealStateDistrictTable
}

