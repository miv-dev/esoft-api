package com.miv.db.entities.demand

import com.miv.db.entities.ClientEntity
import com.miv.db.entities.RealtorEntity
import com.miv.db.entities.UserEntity
import com.miv.db.tables.demand.DemandTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class DemandEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<DemandEntity>(DemandTable)

    val client by ClientEntity referencedOn DemandTable.client
    val realtor by RealtorEntity referencedOn DemandTable.realtor
    val realStateType by DemandTable.realStateType
    val minPrice by DemandTable.minPrice
    val maxPrice by DemandTable.maxPrice
    val minArea by DemandTable.minArea
    val maxArea by DemandTable.maxArea
    val minRooms by DemandTable.minRooms
    val maxRooms by DemandTable.maxRooms
    val minFloor by DemandTable.minFloor
    val maxFloor by DemandTable.maxFloor
    val minFloors by DemandTable.minFloors
    val maxFloors by DemandTable.maxFloors

}