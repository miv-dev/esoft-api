package com.miv.db.entities.estate

import com.miv.db.tables.estate.HouseTable
import com.miv.models.estate.House
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class HouseEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<HouseEntity>(HouseTable) {
        fun get(id: UUID): HouseEntity {
            val compositeID = CompositeID {
                it[HouseTable.realState] = id
            }
            return get(compositeID)
        }

    }

    val realState by EstateEntity referencedOn HouseTable.realState
    var totalFloors by HouseTable.totalFloors
    var totalRooms by HouseTable.totalRooms
    var totalArea by HouseTable.totalArea

    fun toModel() = House(
        estate = realState.toModel(),
        totalArea = totalArea,
        totalRooms = totalRooms,
        totalFloors = totalFloors
    )
}