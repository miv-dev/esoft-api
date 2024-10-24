package com.miv.db.entities.real.state

import com.miv.db.entities.real.state.LandEntity.Companion
import com.miv.db.tables.real.state.ApartmentTable
import com.miv.db.tables.real.state.LandTable
import com.miv.models.real.state.Apartment
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class ApartmentEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<ApartmentEntity>(ApartmentTable){
        fun get(id: UUID): ApartmentEntity {
            val compositeID = CompositeID {
                it[ApartmentTable.realState] = id
            }
            return get(compositeID)
        }
    }

    val realState by RealStateEntity referencedOn ApartmentTable.realState
    var totalArea by ApartmentTable.totalArea
    var totalRooms by ApartmentTable.totalRooms
    var floor by ApartmentTable.floor

    fun toModel() = Apartment(
        realState = realState.toModel(),
        totalArea = totalArea,
        totalRooms  = totalRooms,
        floor = floor
    )
}