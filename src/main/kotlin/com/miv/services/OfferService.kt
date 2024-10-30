package com.miv.services

import com.miv.models.offer.Offer
import java.util.*

interface OfferService {

    suspend fun create(clientID: UUID, realtorID: UUID, realStateID: UUID, price: Int): Offer

    suspend fun getOffers(): List<Offer>

    suspend fun update(id: UUID, clientID: UUID, realtorID: UUID, realStateID: UUID, price: Int): Offer
    suspend fun getOffer(id: UUID): Offer?
    suspend fun delete(id: UUID)

    suspend fun getOffers(userId: UUID): List<Offer>
}