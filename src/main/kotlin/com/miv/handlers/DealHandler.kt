package com.miv.handlers

import com.miv.dto.DealDTO
import com.miv.dto.DemandAndOffers
import com.miv.dto.DemandOffersType
import com.miv.models.deal.Deal

interface DealHandler {
    suspend fun create(dto: DealDTO): Deal
    suspend fun getAll(): List<Deal>
    suspend fun update(dealID: String, dto: DealDTO): Deal
    suspend fun delete(id: String)
    suspend fun getFilters(inSummary: Boolean): DemandAndOffers
}