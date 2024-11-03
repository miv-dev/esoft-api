package com.miv.handlers

import com.miv.dto.OfferDTO
import com.miv.models.offer.OfferClass

interface OfferHandler {

    suspend fun create(offer: OfferDTO): OfferClass

    suspend fun get(
        userID: String?,
        demandID: String?,
        withoutDeal: Boolean = false,
        inSummary: Boolean
    ): List<OfferClass>

    suspend fun getByID(id: String): OfferClass

    suspend fun update(offer: OfferDTO, id: String): OfferClass

    suspend fun delete(id: String)
    suspend fun getWithoutDeals(): List<OfferClass>


}