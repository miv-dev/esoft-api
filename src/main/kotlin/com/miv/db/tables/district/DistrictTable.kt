package com.miv.db.tables.district

import org.jetbrains.exposed.dao.id.IntIdTable

object DistrictTable: IntIdTable("districts") {
    val name = varchar("name", 255)
    val area = text("area")
}