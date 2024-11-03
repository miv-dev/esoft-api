package com.miv.handlers.impl

import com.miv.dto.DealDTO
import com.miv.dto.DemandAndOffers
import com.miv.dto.DemandOffersType
import com.miv.handlers.DealHandler
import com.miv.models.deal.Deal
import com.miv.services.DealService
import com.miv.services.DemandService
import com.miv.services.OfferService
import java.util.*
import javax.inject.Inject

class DealHandlerImpl @Inject constructor(
    private val dealService: DealService,
    private val offersService: OfferService,
    private val demandService: DemandService,
) : DealHandler {

    override suspend fun create(dto: DealDTO): Deal {
        return dealService.create(dto.offerID, dto.demandID)
    }

    override suspend fun getAll(): List<Deal> {
        return dealService.get()
    }

    override suspend fun update(dealID: String, dto: DealDTO): Deal {
        val uuid = UUID.fromString(dealID)

        return dealService.update(uuid, dto.offerID, dto.demandID)
    }

    override suspend fun getFilters(inSummary: Boolean): DemandAndOffers {
        return DemandAndOffers(
            offersService.getOffersWithoutDeals(inSummary),
            demandService.getAll(inSummary = inSummary, withoutDeals = true)
        )
    }

    override suspend fun delete(id: String) {
        val dealID = UUID.fromString(id)
        dealService.delete(dealID)
    }
}