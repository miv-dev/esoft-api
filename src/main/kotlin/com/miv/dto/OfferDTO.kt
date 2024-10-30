package com.miv.dto

import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class OfferDTO(
    @Serializable(with = UUIDSerializer::class)
    val client: UUID,
    @Serializable(with = UUIDSerializer::class)
    val realtor: UUID,
    @Serializable(with = UUIDSerializer::class)
    val realState: UUID,
    val price: Int,
)
