package com.miv.models.user

import com.miv.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("REALTOR")
data class RealtorProfile(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val dealShare: Double?,
    override val avatar: String?
) : Profile()