package com.miv.services.impl

import com.miv.db.entities.demand.DemandEntity
import com.miv.db.entities.offer.OfferEntity
import com.miv.db.tables.deal.DealTable
import com.miv.db.tables.estate.ApartmentTable
import com.miv.db.tables.estate.EstateTable
import com.miv.db.tables.estate.HouseTable
import com.miv.db.tables.estate.LandTable
import com.miv.db.tables.offer.OfferTable
import com.miv.models.offer.Offer
import com.miv.models.offer.OfferClass
import com.miv.models.offer.OfferSummary
import com.miv.services.OfferService
import com.miv.services.EstateService
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID
import javax.inject.Inject

class OfferServiceImpl @Inject constructor(
    private val estateService: EstateService,
) : OfferService {


    override suspend fun getOffers(inSummary: Boolean): List<OfferClass> {
        return newSuspendedTransaction {
            OfferEntity.all().map {
                it.toModel(inSummary)
            }
        }
    }


    override suspend fun getOffer(id: UUID): OfferClass? {
        return newSuspendedTransaction {
            OfferEntity.findById(id)?.toModel()
        }
    }

    override suspend fun getOffersWithoutDeals(isSummary: Boolean) = newSuspendedTransaction {
        val query = OfferTable
            .join(DealTable, JoinType.LEFT)
            .selectAll()
            .where { DealTable.offer.eq(null) }
        OfferEntity.wrapRows(query).map {
            it.toModel(isSummary)
        }
    }

    override suspend fun create(
        clientID: UUID,
        realtorID: UUID,
        realStateID: UUID,
        price: Int
    ): OfferClass {
        val id = newSuspendedTransaction {
            OfferTable.insertAndGetId {
                it[client] = clientID
                it[realtor] = realtorID
                it[realState] = realStateID
                it[OfferTable.price] = price
            }
        }
        return getOffer(id.value)!!
    }

    override suspend fun getOffers(userId: UUID, inSummary: Boolean): List<OfferClass> = newSuspendedTransaction {
        OfferEntity.find {
            OfferTable.client eq userId or (OfferTable.realtor eq userId)
        }.map { it.toModel(inSummary) }
    }

    override suspend fun delete(id: UUID) = newSuspendedTransaction {
        OfferEntity.findById(id)?.delete() ?: throw BadRequestException("No offer found with id $id")
    }

    override suspend fun update(
        id: UUID,
        clientID: UUID,
        realtorID: UUID,
        realStateID: UUID,
        price: Int
    ): OfferClass {
        newSuspendedTransaction {
            OfferTable.update({ OfferTable.id eq id }) {
                it[client] = clientID
                it[realtor] = realtorID
                it[realState] = realStateID
                it[OfferTable.price] = price
            }
        }

        return getOffer(id)!!
    }


    private suspend fun OfferEntity.toModel(isSummary: Boolean = false): OfferClass {
        val name = "Предложение#${id.toString().substring(0, 4)}"

        return if (isSummary) {
            OfferSummary(
                id = id.value,
                name = name,
            )
        } else {
            Offer(
                id = id.value,
                name = name,
                client = client.toModel(),
                realtor = realtor.toModel(),
                estate = estateService.getByID(estate.id.value),
                price = price
            )
        }
    }

    override suspend fun getForDemand(isSummary: Boolean, demandID: UUID): List<OfferClass> =
        newSuspendedTransaction {
            val demand = DemandEntity[demandID]

            val landQuery = LandTable
                .select(LandTable.id, LandTable.realState)

            val apartmentQuery = ApartmentTable
                .select(ApartmentTable.id, ApartmentTable.realState)


            val houseQuery = HouseTable
                .select(HouseTable.id, HouseTable.realState)

            with(demand) {
                landQuery
                    .applyFilter(minArea, maxArea, LandTable.totalArea)
                    .alias("landSubquery")

                houseQuery
                    .applyFilter(minArea, minArea, HouseTable.totalArea)
                    .applyFilter(minRooms, maxRooms, HouseTable.totalRooms)
                    .applyFilter(minFloors, maxFloors, HouseTable.totalFloors)
                    .alias("houseSubquery")

                apartmentQuery
                    .applyFilter(minArea, minArea, ApartmentTable.totalArea)
                    .applyFilter(minRooms, maxRooms, ApartmentTable.totalRooms)
                    .applyFilter(minFloor, maxFloor, ApartmentTable.floor)
                    .alias("apartmentSubquery")
            }

            val query = OfferTable
                .join(DealTable, JoinType.LEFT)
                .innerJoin(EstateTable)
                .selectAll()
                .where {
                    DealTable.offer.isNull() and
                            EstateTable.type.eq(demand.estateType) and
                            (exists(landQuery) or exists(houseQuery) or exists(apartmentQuery))
                }
                .applyFilter(demand.minPrice, demand.maxPrice, OfferTable.price)

            OfferEntity.wrapRows(query).map { it.toModel(isSummary) }
        }

    private fun Query.applyFilter(min: Int?, max: Int?, column: Column<out Number?>): Query {
        min?.let { andWhere { column.greaterEq(it.toDouble()) } }
        max?.let { andWhere { column.lessEq(it.toDouble()) } }
        return this
    }
}