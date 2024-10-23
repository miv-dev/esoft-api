package com.miv.db.entities.real.state

import com.miv.db.tables.real.state.LandTable
import com.miv.models.real.state.Land
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID

class LandEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<LandEntity>(LandTable)

    val realState by RealStateEntity referencedOn LandTable.realState
    val totalArea by LandTable.totalArea

    fun toModel() = Land(
        realState = realState.toModel(),
        totalArea = totalArea,
    )
}