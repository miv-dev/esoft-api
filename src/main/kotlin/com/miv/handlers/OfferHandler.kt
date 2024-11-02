package com.miv.handlers

import com.miv.dto.OfferDTO
import com.miv.models.offer.Offer
import com.miv.models.offer.OfferClass

interface OfferHandler {

    suspend fun create(offer: OfferDTO): OfferClass

    suspend fun get(): List<OfferClass>
    suspend fun get(userID: String): List<OfferClass>
    suspend fun getByID(id: String): OfferClass

    suspend fun update(offer: OfferDTO, id: String): OfferClass

    suspend fun delete(id: String)
    suspend fun getWithoutDeals(): List<OfferClass>


}