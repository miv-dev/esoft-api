package com.miv.db.tables.estate

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object LandTable : CompositeIdTable("lands") {
    val realState = reference("real_state", EstateTable, ReferenceOption.CASCADE)
    val totalArea = double("total_area").nullable()

    init {
        addIdColumn(realState)
    }

    override val primaryKey = PrimaryKey(realState)
}