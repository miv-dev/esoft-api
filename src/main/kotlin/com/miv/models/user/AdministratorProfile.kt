package com.miv.models.user

import com.miv.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("Administrator")
data class AdministratorProfile(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
) : Profile()