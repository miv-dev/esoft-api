package com.miv.services.impl

import com.miv.db.entities.demand.DemandEntity
import com.miv.db.tables.demand.DemandTable
import com.miv.db.tables.demand.DemandTable.client
import com.miv.db.tables.demand.DemandTable.realtor
import com.miv.models.RealStateType
import com.miv.models.demand.Demand
import com.miv.models.offer.Offer
import com.miv.services.DemandService
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.statements.Statement
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*
import javax.inject.Inject

class DemandServiceImpl @Inject constructor() : DemandService {
    override suspend fun create(
        clientID: UUID,
        realtorID: UUID,
        realStateType: RealStateType,
        minPrice: Int?,
        maxPrice: Int?,
        minArea: Int?,
        maxArea: Int?,
        minRooms: Int?,
        maxRooms: Int?,
        minFloor: Int?,
        maxFloor: Int?,
        minFloors: Int?,
        maxFloors: Int?
    ): Demand {
        val id = newSuspendedTransaction {
            DemandTable.insertAndGetId {
                it[client] = clientID
                it[realtor] = realtorID
                it[DemandTable.realStateType] = realStateType
                it[DemandTable.minPrice] = minPrice
                it[DemandTable.maxPrice] = maxPrice
                it[DemandTable.minArea] = minArea
                it[DemandTable.maxArea] = maxArea
                when (realStateType) {
                    RealStateType.HOUSE -> {
                        it[DemandTable.minRooms] = minRooms
                        it[DemandTable.maxRooms] = maxRooms
                        it[DemandTable.minFloors] = minFloors
                        it[DemandTable.maxFloors] = maxFloors
                        it[DemandTable.minFloor] = null
                        it[DemandTable.maxFloor] = null
                    }

                    RealStateType.APARTMENT -> {
                        it[DemandTable.minRooms] = minRooms
                        it[DemandTable.maxRooms] = maxRooms
                        it[DemandTable.minFloor] = minFloor
                        it[DemandTable.maxFloor] = maxFloor
                        it[DemandTable.minFloors] = null
                        it[DemandTable.maxFloors] = null
                    }

                    RealStateType.LAND -> {
                        it[DemandTable.minRooms] = null
                        it[DemandTable.maxRooms] = null
                        it[DemandTable.minFloor] = null
                        it[DemandTable.maxFloor] = null
                        it[DemandTable.minFloors] = null
                        it[DemandTable.maxFloors] = null
                    }
                }
            }
        }

        return getByID(id.value)!!
    }

    override suspend fun get(userID: UUID): List<Demand> = newSuspendedTransaction {
        DemandEntity.find {
            DemandTable.client eq userID or (DemandTable.realtor eq userID)
        }.map { it.toModel() }
    }

    override suspend fun get(): List<Demand> = newSuspendedTransaction {
        DemandEntity.all().map(DemandEntity::toModel)
    }

    override suspend fun update(
        id: UUID,
        clientID: UUID,
        realtorID: UUID,
        realStateType: RealStateType,
        minPrice: Int?,
        maxPrice: Int?,
        minArea: Int?,
        maxArea: Int?,
        minRooms: Int?,
        maxRooms: Int?,
        minFloor: Int?,
        maxFloor: Int?,
        minFloors: Int?,
        maxFloors: Int?
    ): Demand {
        newSuspendedTransaction {
            DemandTable.update({ DemandTable.id eq id }) {
                it[client] = clientID
                it[realtor] = realtorID
                it[DemandTable.realStateType] = realStateType
                it[DemandTable.minPrice] = minPrice
                it[DemandTable.maxPrice] = maxPrice
                it[DemandTable.minArea] = minArea
                it[DemandTable.maxArea] = maxArea
                when (realStateType) {
                    RealStateType.HOUSE -> {
                        it[DemandTable.minRooms] = minRooms
                        it[DemandTable.maxRooms] = maxRooms
                        it[DemandTable.minFloors] = minFloors
                        it[DemandTable.maxFloors] = maxFloors
                        it[DemandTable.minFloor] = null
                        it[DemandTable.maxFloor] = null
                    }

                    RealStateType.APARTMENT -> {
                        it[DemandTable.minRooms] = minRooms
                        it[DemandTable.maxRooms] = maxRooms
                        it[DemandTable.minFloor] = minFloor
                        it[DemandTable.maxFloor] = maxFloor
                        it[DemandTable.minFloors] = null
                        it[DemandTable.maxFloors] = null
                    }

                    RealStateType.LAND -> {
                        it[DemandTable.minRooms] = null
                        it[DemandTable.maxRooms] = null
                        it[DemandTable.minFloor] = null
                        it[DemandTable.maxFloor] = null
                        it[DemandTable.minFloors] = null
                        it[DemandTable.maxFloors] = null
                    }
                }
            }
        }
        return getByID(id)!!
    }

    override suspend fun getByID(id: UUID): Demand? = newSuspendedTransaction {
        DemandEntity.findById(id)?.toModel()
    }


    override suspend fun delete(id: UUID) = newSuspendedTransaction {
        DemandEntity.findById(id)?.delete() ?: throw EntityNotFoundException(
            EntityID(id, DemandTable),
            entity = DemandEntity
        )
    }
}