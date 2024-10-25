package com.miv.models.user

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed class Profile {
    abstract val id: UUID
}