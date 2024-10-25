package com.miv.db.tables.estate

import com.miv.models.EstateType
import org.jetbrains.exposed.dao.id.UUIDTable

object EstateTable : UUIDTable("real_states") {
    val type = enumeration<EstateType>("type")
    val latitude = double("latitude")
    val longitude = double("longitude")
    val addressCity = varchar("address_city", 100).nullable()
    val addressStreet = varchar("address_street", 100).nullable()
    val addressHouse = varchar("address_house", 100).nullable()
    val addressNumber = varchar("address_number", 100).nullable()
}

