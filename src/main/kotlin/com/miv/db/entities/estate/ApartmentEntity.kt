package com.miv.db.entities.estate

import com.miv.db.tables.estate.ApartmentTable
import com.miv.models.estate.Apartment
import com.miv.models.estate.EstateClass
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class ApartmentEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<ApartmentEntity>(ApartmentTable) {
        fun get(id: UUID): ApartmentEntity {
            val compositeID = CompositeID {
                it[ApartmentTable.realState] = id
            }
            return get(compositeID)
        }
    }

    val realState by EstateEntity referencedOn ApartmentTable.realState
    var totalArea by ApartmentTable.totalArea
    var totalRooms by ApartmentTable.totalRooms
    var floor by ApartmentTable.floor

    fun toModel(): EstateClass = Apartment(
        totalArea = totalArea,
        totalRooms = totalRooms,
        floor = floor
    )
}