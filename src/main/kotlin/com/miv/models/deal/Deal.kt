package com.miv.models.deal

import com.miv.models.estate.EstateType
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Deal(
    val type: String = "DEAL",
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val estate: DealObject,
    val buyer: DealClient,
    val seller: DealClient,
    val companyDeduction: Double
)

@Serializable
data class DealObject(
    val price: Double,
    val type: EstateType,
)

@Serializable
data class DealClient(
    val cost: Double,
    val commission: Double,
)
