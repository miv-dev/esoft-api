package com.miv.models.estate

import com.miv.models.district.District
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable()
data class Estate(
    val type: String = "ESTATE",
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val data: EstateClass,
    val latitude: Double,
    val longitude: Double,
    val addressCity: String?,
    val addressStreet: String?,
    val addressHouse: String?,
    val addressNumber: String?,
    val districts: List<District>,
)



