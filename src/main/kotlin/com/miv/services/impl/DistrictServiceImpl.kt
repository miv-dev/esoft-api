package com.miv.services.impl

import com.miv.db.entities.district.DistrictEntity
import com.miv.services.DistrictService
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.inject.Inject

class DistrictServiceImpl @Inject constructor() : DistrictService {
    override suspend fun getDistricts(): List<DistrictEntity> = newSuspendedTransaction {
        DistrictEntity.all().toList()
    }
}