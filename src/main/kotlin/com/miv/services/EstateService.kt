package com.miv.services

import com.miv.models.EstateType
import com.miv.models.estate.EstateClass
import com.miv.models.estate.SortVariant
import org.jetbrains.exposed.sql.SortOrder
import java.util.*

interface EstateService {

    suspend fun search(
        query: String?,
        type: EstateType?,
        districtID: Int?,
        sortByVariant: SortVariant?,
        sortOrder: SortOrder = SortOrder.ASC
    ): List<EstateClass>


    suspend fun create(
        type: EstateType,
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
    ): EstateClass

    suspend fun update(
        id: UUID,
        type: EstateType,
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
    ): EstateClass?

    suspend fun delete(id: UUID)
    suspend fun getByID(id: UUID): EstateClass
}