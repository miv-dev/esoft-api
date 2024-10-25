package com.miv.models.estate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("APARTMENT")
data class Apartment(
    override val estate: Estate,
    val totalArea: Double?,
    val totalRooms: Int?,
    val floor: Int?,
) : EstateClass()