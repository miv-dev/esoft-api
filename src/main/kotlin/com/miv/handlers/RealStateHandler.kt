package com.miv.handlers

import com.miv.dto.RealStateClassDTO
import com.miv.dto.RealStateSearchFiltersDTO
import com.miv.models.district.District
import com.miv.models.real.state.RealState
import com.miv.models.real.state.RealStateClass

interface RealStateHandler {

    suspend fun getFilters(): RealStateSearchFiltersDTO
    suspend fun search(query: String?, type: String?, district: String?, order: String?, orderDirection: String?): List<RealStateClass>
    suspend fun create(data: RealStateClassDTO): RealStateClass
    suspend fun update(id: String, data: RealStateClassDTO): RealStateClass

    suspend fun delete(id: String)
    suspend fun get(id: String): RealStateClass
}