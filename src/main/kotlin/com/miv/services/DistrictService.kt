package com.miv.services

import com.miv.db.entities.district.DistrictEntity

interface DistrictService {
    suspend fun getDistricts(): List<DistrictEntity>

}