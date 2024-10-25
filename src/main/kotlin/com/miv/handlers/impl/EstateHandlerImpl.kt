package com.miv.handlers.impl

import com.miv.dto.RealStateClassDTO
import com.miv.dto.RealStateSearchFiltersDTO
import com.miv.handlers.EstateHandler
import com.miv.models.EstateType
import com.miv.models.estate.EstateClass
import com.miv.models.estate.SortVariant
import com.miv.services.DistrictService
import com.miv.services.EstateService
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*
import javax.inject.Inject

class EstateHandlerImpl @Inject constructor(
    private val districtService: DistrictService,
    private val estateService: EstateService,
) : EstateHandler {
    override suspend fun getFilters(): RealStateSearchFiltersDTO = newSuspendedTransaction {
        val districts = districtService.getDistricts().map { it.toModel() }

        RealStateSearchFiltersDTO(
            districts = districts,
            sortVariants = SortVariant.entries,
            types = EstateType.entries
        )
    }

    override suspend fun search(
        query: String?,
        type: String?,
        district: String?,
        sortBy: String?,
        sordOrder: String?
    ): List<EstateClass> {
        val estateType = type?.let {
            EstateType.valueOf(it)
        }
        val sortByVariant = sortBy?.let {
            SortVariant.valueOf(it)
        }
        val sortOrder = when (sordOrder?.toInt()) {
            -1, 0 -> SortOrder.DESC
            else -> SortOrder.ASC
        }
        val districtID = district?.toInt()

        return estateService.search(
            query,
            estateType,
            districtID,
            sortByVariant,
            sortOrder
        )
    }

    override suspend fun create(data: RealStateClassDTO): EstateClass {
        return estateService.create(
            type = data.type,
            latitude = data.latitude,
            longitude = data.longitude,
            addressCity = data.addressCity,
            addressStreet = data.addressStreet,
            addressHouse = data.addressHouse,
            addressNumber = data.addressNumber,
            totalArea = data.totalArea,
            totalFloors = data.totalFloors,
            totalRooms = data.totalRooms,
            floor = data.floor,
        )
    }

    override suspend fun update(id: String, data: RealStateClassDTO): EstateClass {
        return estateService.update(
            id = UUID.fromString(id),
            type = data.type,
            latitude = data.latitude,
            longitude = data.longitude,
            addressCity = data.addressCity,
            addressStreet = data.addressStreet,
            addressHouse = data.addressHouse,
            addressNumber = data.addressNumber,
            totalArea = data.totalArea,
            totalFloors = data.totalFloors,
            totalRooms = data.totalRooms,
            floor = data.floor,
        ) ?: throw BadRequestException("RealState $id not found")
    }

    override suspend fun delete(id: String) {
        estateService.delete(UUID.fromString(id))
    }

    override suspend fun get(id: String): EstateClass {
        return estateService.getByID(UUID.fromString(id))
    }


}