package com.miv.handlers

import com.miv.dto.DemandDTO
import com.miv.dto.OfferDTO
import com.miv.models.demand.Demand
import com.miv.models.offer.Offer

interface DemandHandler {

    suspend fun create(offer: DemandDTO): Demand

    suspend fun get(): List<Demand>
    suspend fun getByID(id: String): Demand

    suspend fun update(offer: DemandDTO, id: String): Demand

    suspend fun delete(id: String)


}