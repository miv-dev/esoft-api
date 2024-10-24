package com.miv.dto

import com.miv.models.RealStateType
import com.miv.models.district.District
import com.miv.models.real.state.SortVariant
import kotlinx.serialization.Serializable

@Serializable
data class RealStateSearchFiltersDTO(
    val sortVariants: List<SortVariant>,
    val districts: List<District>,
    val types: List<RealStateType>
)

