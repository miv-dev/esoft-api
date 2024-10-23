package com.miv.db.tables.real.state

import com.miv.db.tables.district.DistrictTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RealStateDistrictTable: Table("state_district") {
    val realState = reference("real_state", RealStateTable, onDelete = ReferenceOption.CASCADE)
    val district = reference("district", DistrictTable, onDelete = ReferenceOption.CASCADE)
}