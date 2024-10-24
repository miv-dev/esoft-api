package com.miv.models.offer

import com.miv.models.ClientProfile
import com.miv.models.Profile
import com.miv.models.RealtorProfile
import com.miv.models.real.state.RealStateClass
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Offer(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val client: ClientProfile,
    val realtor: RealtorProfile,
    val realState: RealStateClass,
    val price: Int,
)
