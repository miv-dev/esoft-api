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

    suspend fun get(): List<DemandClass>
    suspend fun get(userID: UUID): List<DemandClass>

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

    suspend fun getByID(id: UUID): DemandClass?
    suspend fun delete(id: UUID)
    suspend fun getWithoutDeals(isSummary: Boolean): List<DemandClass>
}