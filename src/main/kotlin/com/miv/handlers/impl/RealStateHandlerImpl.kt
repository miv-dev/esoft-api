package com.miv.handlers.impl

import com.miv.dto.RealStateClassDTO
import com.miv.dto.RealStateSearchFiltersDTO
import com.miv.handlers.RealStateHandler
import com.miv.models.RealStateType
import com.miv.models.real.state.RealStateClass
import com.miv.models.real.state.SortVariant
import com.miv.services.DistrictService
import com.miv.services.RealStateService
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*
import javax.inject.Inject

class RealStateHandlerImpl @Inject constructor(
    private val districtService: DistrictService,
    private val realStateService: RealStateService,
) : RealStateHandler {
    override suspend fun getFilters(): RealStateSearchFiltersDTO = newSuspendedTransaction {
        val districts = districtService.getDistricts().map { it.toModel() }

        RealStateSearchFiltersDTO(
            districts = districts,
            sortVariants = SortVariant.entries,
            types = RealStateType.entries
        )
    }

    override suspend fun search(
        query: String?,
        type: String?,
        district: String?,
        sortBy: String?,
        sordOrder: String?
    ): List<RealStateClass> {
        val realStateType = type?.let {
            RealStateType.valueOf(it)
        }
        val sortByVariant = sortBy?.let {
            SortVariant.valueOf(it)
        }
        val sortOrder = when (sordOrder?.toInt()) {
            -1, 0 -> SortOrder.DESC
            else -> SortOrder.ASC
        }
        val districtID = district?.toInt()

        return realStateService.search(
            query,
            realStateType,
            districtID,
            sortByVariant,
            sortOrder
        )
    }

    override suspend fun create(data: RealStateClassDTO): RealStateClass {
        return realStateService.create(
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

    override suspend fun update(id: String, data: RealStateClassDTO): RealStateClass {
        return realStateService.update(
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
        realStateService.delete(UUID.fromString(id))
    }

    override suspend fun get(id: String): RealStateClass {
        return realStateService.getByID(UUID.fromString(id))
    }


}