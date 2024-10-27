package com.miv.models.estate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("LAND")
data class Land(
    val totalArea: Double?
) : EstateClass()