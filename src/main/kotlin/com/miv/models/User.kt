package com.miv.models

import com.miv.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val role: Role,
)

@Serializable
open class Profile()

@Serializable
data class ClientProfile(
    val id: Int,
    val user: User,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val phone: String,
    val email: String,
) : Profile()


@Serializable
data class RealtorProfile(
    val id: Int,
    val user: User,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val feePercentage: Double?
) : Profile()


enum class Role {
    ADMIN, CLIENT, REALTOR
}
