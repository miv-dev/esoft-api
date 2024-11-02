package com.miv.models.offer

import com.miv.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@SerialName("OFFER_SUMMARY")
@Serializable
data class OfferSummary(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    override val name: String,
): OfferClass