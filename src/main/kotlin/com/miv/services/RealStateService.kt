package com.miv.services

import com.miv.models.RealStateType
import com.miv.models.real.state.RealStateClass
import com.miv.models.real.state.SortVariant
import org.jetbrains.exposed.sql.SortOrder
import java.util.*

interface RealStateService {

    suspend fun search(
        query: String?,
        type: RealStateType?,
        districtID: Int?,
        sortByVariant: SortVariant?,
        sortOrder: SortOrder = SortOrder.ASC
    ): List<RealStateClass>


    suspend fun create(
        type: RealStateType,
        latitude: Double,
        longitude: Double,
        addressCity: String?,
        addressHouse: String?,
        addressNumber: String?,
        addressStreet: String?,
        totalArea: Double?,
        totalFloors: Int? = null,
        totalRooms: Int? = null,
        floor: Int? = null
    ): RealStateClass

    suspend fun update(
        id: UUID,
        type: RealStateType,
        latitude: Double,
        longitude: Double,
        addressCity: String?,
        addressHouse: String?,
        addressNumber: String?,
        addressStreet: String?,
        totalArea: Double?,
        totalFloors: Int?,
        totalRooms: Int?,
        floor: Int?
    ): RealStateClass?

    suspend fun delete(id: UUID)
    suspend fun getByID(id: UUID): RealStateClass
}