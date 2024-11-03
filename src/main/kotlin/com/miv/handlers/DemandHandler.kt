package com.miv.handlers

import com.miv.dto.DemandDTO
import com.miv.models.demand.Demand
import com.miv.models.demand.DemandClass

interface DemandHandler {

    suspend fun create(offer: DemandDTO): DemandClass

    suspend fun get(userID: String?, offerID: String?, withoutDeals: Boolean, inSummary: Boolean): List<DemandClass>

    suspend fun getByID(id: String): DemandClass

    suspend fun update(offer: DemandDTO, id: String): DemandClass

    suspend fun delete(id: String)

}