package com.miv.handlers.impl

import com.miv.dto.OfferDTO
import com.miv.handlers.OfferHandler
import com.miv.models.offer.OfferClass
import com.miv.services.OfferService
import io.ktor.server.plugins.*
import java.util.*
import javax.inject.Inject

class OfferHandlerImpl @Inject constructor(
    private val service: OfferService
) : OfferHandler {
    override suspend fun create(offer: OfferDTO): OfferClass {
        return service.create(
            offer.client,
            offer.realtor,
            offer.realState,
            offer.price
        )
    }


    override suspend fun get(userID: String?, demandID: String?, withoutDeal: Boolean, inSummary: Boolean): List<OfferClass> {
        when {
            userID != null -> {
                val uuid = UUID.fromString(userID)
                return service.getOffers(uuid, inSummary)
            }
            demandID != null -> {
                val uuid = UUID.fromString(demandID)
                return service.getForDemand(isSummary = inSummary, uuid)
            }
            else-> {
                return if (withoutDeal){
                    service.getOffersWithoutDeals(inSummary)
                }else{
                    service.getOffers(inSummary)
                }
            }
        }
    }

    override suspend fun getByID(id: String): OfferClass {
        val uuid = UUID.fromString(id)
        return service.getOffer(uuid) ?: throw NotFoundException("Offer with id:$id not found")
    }

    override suspend fun update(offer: OfferDTO, id: String): OfferClass {
        val uuid = UUID.fromString(id)
        return service.update(
            uuid,
            offer.client,
            offer.realtor,
            offer.realState,
            offer.price
        )
    }

    override suspend fun getWithoutDeals(): List<OfferClass> {
        return service.getOffersWithoutDeals(true)
    }

    override suspend fun delete(id: String) {
        val uuid = UUID.fromString(id)
        service.delete(uuid)
    }

}