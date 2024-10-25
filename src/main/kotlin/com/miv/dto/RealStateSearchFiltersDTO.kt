package com.miv.dto

import com.miv.models.EstateType
import com.miv.models.district.District
import com.miv.models.estate.SortVariant
import kotlinx.serialization.Serializable

@Serializable
data class RealStateSearchFiltersDTO(
    val sortVariants: List<SortVariant>,
    val districts: List<District>,
    val types: List<EstateType>
)

