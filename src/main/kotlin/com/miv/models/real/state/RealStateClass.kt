package com.miv.models.real.state

import kotlinx.serialization.Serializable

@Serializable
sealed class RealStateClass {
    abstract val realState: RealState
}