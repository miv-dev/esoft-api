package com.miv.db.tables.deal

import com.miv.db.tables.demand.DemandTable
import com.miv.db.tables.offer.OfferTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object DealTable : UUIDTable("deals", "deal_id") {
    val offer = reference("offer_id", OfferTable, onDelete = ReferenceOption.RESTRICT)
    val demand = reference("demand_id", DemandTable, onDelete = ReferenceOption.RESTRICT)
}