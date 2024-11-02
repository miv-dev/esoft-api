package com.miv.models.demand

import com.miv.models.estate.EstateType
import com.miv.models.user.ClientProfile
import com.miv.models.user.RealtorProfile
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@SerialName("DEMAND")
@Serializable
data class Demand(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    override val name: String,
    val client: ClientProfile,
    val realtor: RealtorProfile,
    val estateType: EstateType,
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
) : DemandClass

@Serializable
sealed interface DemandClass {
    val id: UUID
    val name: String
}
