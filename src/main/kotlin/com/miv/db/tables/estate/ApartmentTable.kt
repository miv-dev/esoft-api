package com.miv.db.tables.estate

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ApartmentTable : CompositeIdTable("apartments") {
    val realState = reference("real_state", EstateTable, ReferenceOption.CASCADE)
    val totalArea = double("total_area").nullable()
    val totalRooms = integer("total_rooms").nullable()
    val floor = integer("floor").nullable()

    init {
        addIdColumn(realState)
    }

    override val primaryKey = PrimaryKey(realState)
}