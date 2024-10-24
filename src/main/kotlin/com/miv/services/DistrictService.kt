package com.miv.services

import com.miv.db.entities.district.DistrictEntity
import org.jetbrains.exposed.sql.SizedIterable

interface DistrictService {
    suspend fun getDistricts(): List<DistrictEntity>
    suspend fun findDistrictByCoords(lat: Double, lng: Double): SizedIterable<DistrictEntity>
}