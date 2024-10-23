package com.miv.models.real.state

import com.miv.models.RealStateType
import com.miv.models.district.District
import java.util.UUID

sealed class RealState {
    abstract val id: UUID
    abstract val type: RealStateType
    abstract val latitude: Double
    abstract val longitude: Double
    abstract val addressCity: String?
    abstract val addressStreet: String?
    abstract val addressHouse: String?
    abstract val addressNumber: String?
    abstract val districts: List<District>
}


