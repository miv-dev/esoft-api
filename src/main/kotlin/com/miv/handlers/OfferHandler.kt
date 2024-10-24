package com.miv.handlers

import com.miv.dto.OfferDTO
import com.miv.models.offer.Offer

interface OfferHandler {

    suspend fun create(offer: OfferDTO): Offer

    suspend fun get(): List<Offer>
    suspend fun getByID(id: String): Offer

    suspend fun update(offer: OfferDTO, id: String): Offer

    suspend fun delete(id: String)



}