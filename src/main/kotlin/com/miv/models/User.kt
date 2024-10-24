package com.miv.models

import com.miv.utils.UUIDSerializer
import kotlinx.serialization.SerialName
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
    abstract val user: User
}

@Serializable
@SerialName("Client")
data class ClientProfile(
    override val user: User,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val phone: String?,
    val email: String?,
) : Profile()


@Serializable
@SerialName("Realtor")
data class RealtorProfile(
    override val user: User,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val dealShare: Double?
) : Profile()


@Serializable
@SerialName("Administrator")
data class AdministratorProfile(
    override val user: User
) : Profile()

enum class Role {
    CLIENT, REALTOR, ADMIN
}
