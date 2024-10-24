package com.miv.models.district

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class District(
    val id: Int,
    val name: String,
)