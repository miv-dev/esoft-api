package com.miv.services

import com.miv.models.deal.Deal
import java.util.*

interface DealService {
    suspend fun get(): List<Deal>
    suspend fun create(offerID: UUID, demandID: UUID): Deal
    suspend fun update(dealID: UUID, offerID: UUID, demandID: UUID): Deal
    suspend fun delete(id: UUID)
}