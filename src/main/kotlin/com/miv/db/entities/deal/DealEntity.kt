package com.miv.db.entities.deal

import com.miv.db.entities.demand.DemandEntity
import com.miv.db.entities.offer.OfferEntity
import com.miv.db.tables.deal.DealTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class DealEntity(id: EntityID<UUID>) : UUIDEntity(id)  {
    companion object : UUIDEntityClass<DealEntity>(DealTable)

    var offer by OfferEntity referencedOn DealTable.offer
    var demand by DemandEntity referencedOn DealTable.demand

}