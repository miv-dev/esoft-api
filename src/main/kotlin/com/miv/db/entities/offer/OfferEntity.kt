package com.miv.db.entities.offer

import com.miv.db.entities.ClientEntity
import com.miv.db.entities.RealtorEntity
import com.miv.db.entities.estate.EstateEntity
import com.miv.db.tables.offer.OfferTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class OfferEntity(id: EntityID<UUID>) : UUIDEntity(id) {


    companion object : UUIDEntityClass<OfferEntity>(OfferTable)

    val client by ClientEntity referencedOn OfferTable.client
    val realtor by RealtorEntity referencedOn OfferTable.realtor
    val estate by EstateEntity referencedOn OfferTable.realState
    val price by OfferTable.price
}