package com.miv.models.estate

import com.miv.models.EstateType
import com.miv.models.district.District
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Estate(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val type: EstateType,
    val latitude: Double,
    val longitude: Double,
    val addressCity: String?,
    val addressStreet: String?,
    val addressHouse: String?,
    val addressNumber: String?,
    val districts: List<District>
)



