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
sealed class Profile {
    abstract val id: Int
    abstract val user: User
}

@Serializable
data class ClientProfile(
    override val id: Int,
    override val user: User,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val phone: String?,
    val email: String?,
) : Profile()


@Serializable
data class RealtorProfile(
    override val id: Int,
    override val user: User,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val dealShare: Double?
) : Profile()


enum class Role {
    CLIENT, REALTOR, ADMIN
}
