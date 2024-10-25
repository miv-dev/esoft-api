package com.miv.handlers

import com.miv.dto.RealStateClassDTO
import com.miv.dto.RealStateSearchFiltersDTO
import com.miv.models.estate.EstateClass

interface EstateHandler {

    suspend fun getFilters(): RealStateSearchFiltersDTO
    suspend fun search(query: String?, type: String?, district: String?, order: String?, orderDirection: String?): List<EstateClass>
    suspend fun create(data: RealStateClassDTO): EstateClass
    suspend fun update(id: String, data: RealStateClassDTO): EstateClass

    suspend fun delete(id: String)
    suspend fun get(id: String): EstateClass
}