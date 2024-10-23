package com.miv.models.real.state

import kotlinx.serialization.Serializable

@Serializable
data class Apartment(
    override val realState: RealState,
    val totalArea: Double?,
    val rooms: Int?,
    val floor: Int?,
) : RealStateClass()