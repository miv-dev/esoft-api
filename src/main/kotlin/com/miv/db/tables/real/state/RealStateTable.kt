package com.miv.db.tables.real.state

import org.jetbrains.exposed.dao.id.UUIDTable

object RealStateTable : UUIDTable("real_states") {
    val addressCity = varchar("address_city", 100).nullable()
    val addressStreet = varchar("address_street", 100).nullable()
    val addressHouse = varchar("address_house", 100).nullable()
    val addressNumber = varchar("address_number", 100).nullable()
    val latitude = double("latitude").nullable()
    val longitude = double("longitude").nullable()
}


