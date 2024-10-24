package com.miv.handlers.impl

import com.miv.dto.OfferDTO
import com.miv.handlers.OfferHandler
import com.miv.models.offer.Offer
import com.miv.services.OfferService
import io.ktor.server.plugins.*
import java.util.*
import javax.inject.Inject

class OfferHandlerImpl @Inject constructor(
    private val service: OfferService
) : OfferHandler {
    override suspend fun create(offer: OfferDTO): Offer {
        return service.create(
            offer.client,
            offer.realtor,
            offer.realState,
            offer.price
        )
    }

    override suspend fun get(): List<Offer> {
        return service.getOffers()
    }

    override suspend fun getByID(id: String): Offer {
        val uuid = UUID.fromString(id)
        return service.getOffer(uuid) ?: throw NotFoundException("Offer with id:$id not found")
    }

    override suspend fun update(offer: OfferDTO, id: String): Offer {
        val uuid = UUID.fromString(id)
        return service.update(
            uuid,
            offer.client,
            offer.realtor,
            offer.realState,
            offer.price
        )
    }

    override suspend fun delete(id: String) {
        val uuid = UUID.fromString(id)
        service.delete(uuid)
    }

}