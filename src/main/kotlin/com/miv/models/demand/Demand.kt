package com.miv.models.demand

import com.miv.models.ClientProfile
import com.miv.models.RealStateType
import com.miv.models.RealtorProfile
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Demand(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val client: ClientProfile,
    val realtor: RealtorProfile,
    val realStateType: RealStateType,
    val minPrice: Int?,
    val maxPrice: Int?,
    val minArea: Int?,
    val maxArea: Int?,
    val minRooms: Int?,
    val maxRooms: Int?,
    val minFloor: Int?,
    val maxFloor: Int?,
    val minFloors: Int?,
    val maxFloors: Int?,
)
