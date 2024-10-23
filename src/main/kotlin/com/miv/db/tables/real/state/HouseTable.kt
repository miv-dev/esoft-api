package com.miv.db.tables.real.state

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object HouseTable : CompositeIdTable("houses") {
    val realState = reference("real_state", RealStateTable, ReferenceOption.CASCADE)
    val totalFloors = integer("total_floors").nullable()
    val totalArea = double("total_areas").nullable()

    init {
        addIdColumn(realState)
    }

    override val primaryKey = PrimaryKey(realState)
}