package com.miv.models.estate

import kotlinx.serialization.Serializable

@Serializable
sealed class EstateClass {
    abstract val estate: Estate
}