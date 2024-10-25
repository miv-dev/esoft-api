package com.miv.db.tables.demand

import com.miv.db.tables.ClientProfileTable
import com.miv.db.tables.RealtorProfileTable
import com.miv.models.estate.EstateType
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption


object DemandTable : UUIDTable("demands") {
    val client = reference("client_id", ClientProfileTable, onDelete = ReferenceOption.CASCADE)
    val realtor = reference("realtor_id", RealtorProfileTable, onDelete = ReferenceOption.CASCADE)
    val estateType = enumeration<EstateType>("real_state_type")

    val minPrice = integer("min_price").nullable()
    val maxPrice = integer("max_price").nullable()

    val minArea = integer("min_area").nullable()
    val maxArea = integer("max_area").nullable()

    val minRooms = integer("min_rooms").nullable()
    val maxRooms = integer("max_rooms").nullable()

    val minFloor = integer("min_floor").nullable()
    val maxFloor = integer("max_floor").nullable()

    val minFloors = integer("min_floors").nullable()
    val maxFloors = integer("max_floors").nullable()
}