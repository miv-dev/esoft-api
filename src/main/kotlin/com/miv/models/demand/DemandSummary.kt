package com.miv.models.demand

import com.miv.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@SerialName("DEMAND_SUMMARY")
@Serializable
data class DemandSummary(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    override val name: String,
) : DemandClass