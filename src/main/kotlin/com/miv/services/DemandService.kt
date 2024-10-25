package com.miv.services

import com.miv.models.RealStateType
import com.miv.models.demand.Demand
import com.miv.models.offer.Offer
import java.util.*

interface DemandService {

    suspend fun create(
        clientID: UUID,
        realtorID: UUID,
        realStateType: RealStateType,
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

    suspend fun update(
        id: UUID,
        clientID: UUID,
        realtorID: UUID,
        realStateType: RealStateType,
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