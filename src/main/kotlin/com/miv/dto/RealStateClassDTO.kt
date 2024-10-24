package com.miv.dto

import com.miv.models.RealStateType
import com.miv.models.district.District
import kotlinx.serialization.Serializable

@Serializable
data class RealStateClassDTO(
    val type: RealStateType,
    val latitude: Double,
    val longitude: Double,
    val addressCity: String?,
    val addressStreet: String?,
    val addressHouse: String?,
    val addressNumber: String?,
    val totalArea: Double? = null,
    val totalFloors: Int? = null,
    val totalRooms: Int? = null,
    val floor: Int? = null,
)
