package com.miv.models.real.state

import com.miv.models.RealStateType
import com.miv.models.district.District
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Land(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    override val type: RealStateType,
    override val latitude: Double,
    override val longitude: Double,
    override val addressCity: String?,
    override val addressStreet: String?,
    override val addressHouse: String?,
    override val addressNumber: String?,
    override val districts: List<District>,
    val totalArea: Double?
) : RealState()