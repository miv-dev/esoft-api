package com.miv.db.tables.real.state

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object LandTable : CompositeIdTable("lands") {
    val realState = reference("real_state", RealStateTable, ReferenceOption.CASCADE)
    val totalArea = double("total_areas").nullable()

    init {
        addIdColumn(realState)
    }

    override val primaryKey = PrimaryKey(realState)
}