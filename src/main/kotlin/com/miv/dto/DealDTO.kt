package com.miv.dto

import com.miv.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class DealDTO(
    @SerialName("demandId")
    @Serializable(with = UUIDSerializer::class)
    val demandID: UUID,

    @SerialName("offerId")
    @Serializable(with = UUIDSerializer::class)
    val offerID: UUID
)
