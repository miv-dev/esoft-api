package com.miv.services

import com.miv.models.offer.Offer
import com.miv.models.offer.OfferClass
import java.util.*

interface OfferService {

    suspend fun create(clientID: UUID, realtorID: UUID, realStateID: UUID, price: Int): OfferClass

    suspend fun getOffer(id: UUID): OfferClass?
    suspend fun getOffers(inSummary: Boolean): List<OfferClass>
    suspend fun getOffers(userId: UUID, inSummary: Boolean): List<OfferClass>
    suspend fun getOffersWithoutDeals(isSummary: Boolean): List<OfferClass>
    suspend fun getForDemand(isSummary: Boolean, demandID: UUID): List<OfferClass>

    suspend fun update(id: UUID, clientID: UUID, realtorID: UUID, realStateID: UUID, price: Int): OfferClass
    suspend fun delete(id: UUID)

}