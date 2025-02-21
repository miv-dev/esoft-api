package com.miv.models.user

import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val role: Role,
    val avatar: String?
)


