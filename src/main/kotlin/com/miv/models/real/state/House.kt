package com.miv.models.real.state

import com.miv.models.RealStateType
import com.miv.models.district.District
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class House(
    override val realState: RealState,
    val totalArea: Double?,
    val totalFloors: Int?
) : RealStateClass()