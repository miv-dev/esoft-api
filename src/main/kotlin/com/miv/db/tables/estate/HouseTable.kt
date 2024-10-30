package com.miv.db.tables.estate

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object HouseTable : CompositeIdTable("houses") {
    val realState = reference("real_state", EstateTable, ReferenceOption.CASCADE)
    val totalFloors = integer("total_floors").nullable()
    val totalRooms = integer("total_rooms").nullable()
    val totalArea = double("total_area").nullable()

    init {
        addIdColumn(realState)
    }

    override val primaryKey = PrimaryKey(realState)
}