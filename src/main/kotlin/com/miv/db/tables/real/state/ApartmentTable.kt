package com.miv.db.tables.real.state

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ApartmentTable : CompositeIdTable("apartments") {
    val realState = reference("real_state", RealStateTable, ReferenceOption.CASCADE)
    val totalArea = double("total_area").nullable()
    val rooms = integer("rooms").nullable()
    val floor = integer("floor").nullable()

    init {
        addIdColumn(realState)
    }

    override val primaryKey = PrimaryKey(realState)
}