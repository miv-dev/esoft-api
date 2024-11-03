package com.miv.models.offer

import com.miv.models.estate.Estate
import com.miv.models.user.Profile
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@SerialName("OFFER")
@Serializable
data class Offer(
    override val name: String,
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    val client: Profile,
    val realtor: Profile,
    val estate: Estate,
    val price: Int,
) : OfferClass

@Serializable
sealed interface OfferClass {
    val id: UUID
    val name: String
}
