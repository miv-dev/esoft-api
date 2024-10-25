package com.miv.dto

import com.miv.models.EstateType
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class DemandDTO(
    @Serializable(with = UUIDSerializer::class)
    val client: UUID,
    @Serializable(with = UUIDSerializer::class)
    val realtor: UUID,
    val estateType: EstateType,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val minArea: Int? = null,
    val maxArea: Int? = null,
    val minRooms: Int? = null,
    val maxRooms: Int? = null,
    val minFloor: Int? = null,
    val maxFloor: Int? = null,
    val minFloors: Int? = null,
    val maxFloors: Int? = null,
)
