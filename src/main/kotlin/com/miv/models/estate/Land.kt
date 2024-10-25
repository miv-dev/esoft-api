package com.miv.models.estate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("LAND")
data class Land(
    override val estate: Estate,
    val totalArea: Double?
) : EstateClass()