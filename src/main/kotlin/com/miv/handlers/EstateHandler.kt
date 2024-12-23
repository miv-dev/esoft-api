package com.miv.handlers

import com.miv.dto.EstateClassDTO
import com.miv.dto.EstateSearchFiltersDTO
import com.miv.models.estate.Estate
import com.miv.models.estate.EstateClass

interface EstateHandler {

    suspend fun getFilters(): EstateSearchFiltersDTO
    suspend fun search(query: String?, type: String?, district: String?, sortBy: String?, sortOrder: String?): List<Estate>
    suspend fun create(data: EstateClassDTO): Estate
    suspend fun update(id: String, data: EstateClassDTO): Estate

    suspend fun delete(id: String)
    suspend fun get(id: String): Estate
}