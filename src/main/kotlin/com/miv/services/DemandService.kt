package com.miv.services

import com.miv.models.estate.EstateType
import com.miv.models.demand.Demand
import com.miv.models.demand.DemandClass
import java.util.*

interface DemandService {

    suspend fun create(
        clientID: UUID,
        realtorID: UUID,
        estateType: EstateType,
        minPrice: Int?,
        maxPrice: Int?,
        minArea: Int?,
        maxArea: Int?,
        minRooms: Int?,
        maxRooms: Int?,
        minFloor: Int?,
        maxFloor: Int?,
        minFloors: Int?,
        maxFloors: Int?,
    ): DemandClass

    suspend fun getAll(inSummary: Boolean, withoutDeals: Boolean): List<DemandClass>
    suspend fun getByID(id: UUID): DemandClass?
    suspend fun getByUserID(userID: UUID, inSummary: Boolean): List<DemandClass>
    suspend fun getForOffer(offerID: UUID, inSummary: Boolean): List<DemandClass>

    suspend fun update(
        id: UUID,
        clientID: UUID,
        realtorID: UUID,
        estateType: EstateType,
        minPrice: Int?,
        maxPrice: Int?,
        minArea: Int?,
        maxArea: Int?,
        minRooms: Int?,
        maxRooms: Int?,
        minFloor: Int?,
        maxFloor: Int?,
        minFloors: Int?,
        maxFloors: Int?,
    ): DemandClass

    suspend fun delete(id: UUID)
}