package com.miv.models.estate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("HOUSE")
data class House(
    val totalArea: Double?,
    val totalRooms: Int?,
    val totalFloors: Int?
) : EstateClass()