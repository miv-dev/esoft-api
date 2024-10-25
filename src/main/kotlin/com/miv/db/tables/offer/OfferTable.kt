package com.miv.db.tables.offer

import com.miv.db.tables.ClientProfileTable
import com.miv.db.tables.RealtorProfileTable
import com.miv.db.tables.estate.EstateTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object OfferTable: UUIDTable("offers") {
    val client = reference("client_id", ClientProfileTable, onDelete = ReferenceOption.CASCADE)
    val realtor = reference("realtor_id", RealtorProfileTable, onDelete = ReferenceOption.CASCADE)
    val realState = reference("real_state_id", EstateTable, onDelete = ReferenceOption.CASCADE)
    val price = integer("price")
}
