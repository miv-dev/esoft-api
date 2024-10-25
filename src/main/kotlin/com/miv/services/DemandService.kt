package com.miv.services

import com.miv.models.estate.EstateType
import com.miv.models.demand.Demand
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
    ): Demand

    suspend fun get(): List<Demand>
    suspend fun get(userID: UUID): List<Demand>

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
    ): Demand

    suspend fun getByID(id: UUID): Demand?
    suspend fun delete(id: UUID)
}