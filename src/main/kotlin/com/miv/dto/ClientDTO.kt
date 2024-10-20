package com.miv.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClientDTO(
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val phone: String?,
    val email: String?,
)

