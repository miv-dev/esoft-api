package com.miv.db.entities.real.state

import com.miv.db.tables.real.state.ApartmentTable
import com.miv.models.real.state.Apartment
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID

class ApartmentEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<ApartmentEntity>(ApartmentTable)

    val realState by RealStateEntity referencedOn ApartmentTable.realState
    val totalArea by ApartmentTable.totalArea
    val rooms by ApartmentTable.rooms
    val floor by ApartmentTable.floor

    fun toModel() = Apartment(
        realState = realState.toModel(),
        totalArea = totalArea,
        rooms = rooms,
        floor = floor
    )
}