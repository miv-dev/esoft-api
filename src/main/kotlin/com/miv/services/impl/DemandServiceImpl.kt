package com.miv.services.impl

import com.miv.db.entities.demand.DemandEntity
import com.miv.db.entities.offer.OfferEntity
import com.miv.db.tables.deal.DealTable
import com.miv.db.tables.demand.DemandTable
import com.miv.db.tables.estate.ApartmentTable
import com.miv.db.tables.estate.EstateTable
import com.miv.db.tables.estate.HouseTable
import com.miv.db.tables.estate.LandTable
import com.miv.db.tables.offer.OfferTable
import com.miv.models.estate.EstateType
import com.miv.models.demand.Demand
import com.miv.models.demand.DemandClass
import com.miv.models.estate.Apartment
import com.miv.models.estate.House
import com.miv.models.estate.Land
import com.miv.services.DemandService
import com.miv.services.EstateService
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*
import javax.inject.Inject

class DemandServiceImpl @Inject constructor(
    private val estateService: EstateService,
) : DemandService {
    override suspend fun create(
        clientID: UUID,
        realtorID: UUID,
        estateType: EstateType,
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
    ): DemandClass {
        val id = newSuspendedTransaction {
            DemandTable.insertAndGetId {
                it[client] = clientID
                it[realtor] = realtorID
                it[DemandTable.estateType] = estateType
                it[DemandTable.minPrice] = minPrice
                it[DemandTable.maxPrice] = maxPrice
                it[DemandTable.minArea] = minArea
                it[DemandTable.maxArea] = maxArea
                when (estateType) {
                    EstateType.HOUSE -> {
                        it[DemandTable.minRooms] = minRooms
                        it[DemandTable.maxRooms] = maxRooms
                        it[DemandTable.minFloors] = minFloors
                        it[DemandTable.maxFloors] = maxFloors
                        it[DemandTable.minFloor] = null
                        it[DemandTable.maxFloor] = null
                    }

                    EstateType.APARTMENT -> {
                        it[DemandTable.minRooms] = minRooms
                        it[DemandTable.maxRooms] = maxRooms
                        it[DemandTable.minFloor] = minFloor
                        it[DemandTable.maxFloor] = maxFloor
                        it[DemandTable.minFloors] = null
                        it[DemandTable.maxFloors] = null
                    }

                    EstateType.LAND -> {
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

    override suspend fun update(
        id: UUID,
        clientID: UUID,
        realtorID: UUID,
        estateType: EstateType,
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
    ): DemandClass {
        newSuspendedTransaction {
            DemandTable.update({ DemandTable.id eq id }) {
                it[client] = clientID
                it[realtor] = realtorID
                it[DemandTable.estateType] = estateType
                it[DemandTable.minPrice] = minPrice
                it[DemandTable.maxPrice] = maxPrice
                it[DemandTable.minArea] = minArea
                it[DemandTable.maxArea] = maxArea
                when (estateType) {
                    EstateType.HOUSE -> {
                        it[DemandTable.minRooms] = minRooms
                        it[DemandTable.maxRooms] = maxRooms
                        it[DemandTable.minFloors] = minFloors
                        it[DemandTable.maxFloors] = maxFloors
                        it[DemandTable.minFloor] = null
                        it[DemandTable.maxFloor] = null
                    }

                    EstateType.APARTMENT -> {
                        it[DemandTable.minRooms] = minRooms
                        it[DemandTable.maxRooms] = maxRooms
                        it[DemandTable.minFloor] = minFloor
                        it[DemandTable.maxFloor] = maxFloor
                        it[DemandTable.minFloors] = null
                        it[DemandTable.maxFloors] = null
                    }

                    EstateType.LAND -> {
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

    override suspend fun getByID(id: UUID): DemandClass? = newSuspendedTransaction {
        DemandEntity.findById(id)?.toModel()
    }

    override suspend fun getAll(inSummary: Boolean, withoutDeals: Boolean): List<DemandClass> =
        newSuspendedTransaction {
            val query = if (withoutDeals) {
                DemandTable
                    .join(DealTable, JoinType.LEFT)
                    .selectAll()
                    .where {
                        DealTable.offer.isNull()
                    }
            } else {
                DemandTable.selectAll()
            }
            DemandEntity.wrapRows(query).map { it.toModel(inSummary) }
        }

    override suspend fun getByUserID(userID: UUID, inSummary: Boolean): List<DemandClass> =
        newSuspendedTransaction {
            val query = DemandTable.selectAll()
                .where { DemandTable.client eq userID or (DemandTable.realtor eq userID) }

            DemandEntity.wrapRows(query).map { it.toModel(inSummary) }
        }


    override suspend fun getForOffer(offerID: UUID, inSummary: Boolean): List<DemandClass> =
        newSuspendedTransaction {
            val offer = OfferEntity[offerID]

            val query = DemandTable
                .join(DealTable, JoinType.LEFT)
                .selectAll()
                .where {
                    DealTable.offer.isNull()
                }
                .andWhere { DemandTable.estateType.eq(offer.estate.type) }
                .applyFilter(DemandTable.minPrice, DemandTable.maxPrice, offer.price)

            estateService.getByID(offer.estate.id.value).also { estate ->
                when (val data = estate.data) {
                    is Apartment -> query
                        .applyFilter(DemandTable.minArea, DemandTable.maxArea, data.totalArea)
                        .applyFilter(DemandTable.minRooms, DemandTable.maxRooms, data.totalRooms)
                        .applyFilter(DemandTable.minFloor, DemandTable.maxFloor, data.floor)

                    is House -> query
                        .applyFilter(DemandTable.minArea, DemandTable.maxArea, data.totalArea)
                        .applyFilter(DemandTable.minRooms, DemandTable.maxRooms, data.totalRooms)
                        .applyFilter(DemandTable.minFloors, DemandTable.maxFloor, data.totalFloors)

                    is Land -> query
                        .applyFilter(DemandTable.minArea, DemandTable.maxArea, data.totalArea)

                }
            }


            DemandEntity.wrapRows(query).map { it.toModel(inSummary) }
        }


    override suspend fun delete(id: UUID) = newSuspendedTransaction {
        DemandEntity.findById(id)?.delete() ?: throw EntityNotFoundException(
            EntityID(id, DemandTable),
            entity = DemandEntity
        )
    }


    private fun Query.applyFilter(minCol: Column<out Number?>, maxCol: Column<out Number?>, value: Number?): Query {
        value?.let { andWhere { minCol.lessEq(it.toInt()) } }
        value?.let { andWhere { maxCol.greaterEq(it.toInt()) } }
        return this
    }
}