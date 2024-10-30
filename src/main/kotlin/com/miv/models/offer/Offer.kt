package com.miv.models.offer

import com.miv.models.estate.Estate
import com.miv.models.user.Profile
import com.miv.models.estate.EstateClass
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Offer(
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val client: Profile,
    val realtor: Profile,
    val estate: Estate,
    val price: Int,
)
