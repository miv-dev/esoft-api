package com.miv.db.entities.real.state

import com.miv.db.tables.real.state.HouseTable
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID

class HouseEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<HouseEntity>(HouseTable)

    val realState by RealStateEntity referencedOn HouseTable.realState
    val totalFloors by HouseTable.totalFloors
    val totalArea by HouseTable.totalArea
}