package com.miv.dto

import kotlinx.serialization.Serializable

@Serializable
data class RealtorDTO(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val dealShare: Double?
)
