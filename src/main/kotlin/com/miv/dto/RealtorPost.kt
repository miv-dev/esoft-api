package com.miv.dto

import kotlinx.serialization.Serializable

@Serializable
data class RealtorPost(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val dealShare: Double?
)
