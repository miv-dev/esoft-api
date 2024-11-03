package com.miv.handlers.impl

import com.miv.db.entities.demand.DemandEntity
import com.miv.db.tables.demand.DemandTable
import com.miv.dto.DemandDTO
import com.miv.handlers.DemandHandler
import com.miv.models.demand.Demand
import com.miv.models.demand.DemandClass
import com.miv.services.DemandService
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import javax.inject.Inject

class DemandHandlerImpl @Inject constructor(
    private val service: DemandService,
) : DemandHandler {
    override suspend fun create(offer: DemandDTO): DemandClass {
        return with(offer) {
            service.create(
                client,
                realtor,
                estateType,
                minPrice,
                maxPrice,
                minArea,
                maxArea,
                minRooms,
                maxRooms,
                minFloor,
                maxFloor,
                minFloors,
                maxFloors,
            )
        }
    }

    override suspend fun get(
        userID: String?,
        offerID: String?,
        withoutDeals: Boolean,
        inSummary: Boolean
    ): List<DemandClass> {
        userID?.let {
            val id = UUID.fromString(userID)

            return service.getByUserID(id, inSummary)
        }

        offerID?.let {
            val id = UUID.fromString(offerID)

            return service.getForOffer(id, inSummary)
        }

        return service.getAll(inSummary, withoutDeals)
    }


    override suspend fun getByID(id: String): DemandClass {
        val uuid = UUID.fromString(id)
        return service.getByID(uuid) ?: throw EntityNotFoundException(EntityID(uuid, DemandTable), DemandEntity)
    }

    override suspend fun update(offer: DemandDTO, id: String): DemandClass {
        val uuid = UUID.fromString(id)
        return with(offer) {
            service.update(
                uuid,
                client,
                realtor,
                estateType,
                minPrice,
                maxPrice,
                minArea,
                maxArea,
                minRooms,
                maxRooms,
                minFloor,
                maxFloor,
                minFloors,
                maxFloors,
            )
        }
    }

    override suspend fun delete(id: String) {
        val uuid = UUID.fromString(id)
        service.delete(uuid)
    }

}