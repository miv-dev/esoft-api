package com.miv.db.entities.district

import com.miv.db.tables.district.DistrictTable
import com.miv.models.district.District
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DistrictEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DistrictEntity>(DistrictTable)

    var name by DistrictTable.name
    var area by DistrictTable.area

    fun toModel() = District(
        id = id.value,
        name = name,
    )
}