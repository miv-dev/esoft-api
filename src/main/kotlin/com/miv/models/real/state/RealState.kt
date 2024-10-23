package com.miv.models.real.state

import com.miv.models.RealStateType
import com.miv.models.district.District
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class RealState(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val type: RealStateType,
    val latitude: Double,
    val longitude: Double,
    val addressCity: String?,
    val addressStreet: String?,
    val addressHouse: String?,
    val addressNumber: String?,
    val districts: List<District>
)


