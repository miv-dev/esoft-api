package com.miv.db.tables.offer

import com.miv.db.tables.UserTable
import com.miv.db.tables.real.state.RealStateTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object OfferTable: UUIDTable("offers") {
    val client = reference("client_id", UserTable, onDelete = ReferenceOption.CASCADE)
    val realtor = reference("realtor_id", UserTable, onDelete = ReferenceOption.CASCADE)
    val realState = reference("real_state_id", RealStateTable, onDelete = ReferenceOption.CASCADE)
    val price = integer("price")
}
