package com.miv.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClientPost(
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val phone: String?,
    val email: String?,
)
