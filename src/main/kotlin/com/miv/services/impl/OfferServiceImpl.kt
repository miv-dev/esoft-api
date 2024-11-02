package com.miv.services.impl

import com.miv.db.entities.offer.OfferEntity
import com.miv.db.tables.deal.DealTable
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


    override suspend fun getOffers(): List<OfferClass> {
        return newSuspendedTransaction {
            OfferEntity.all().map {
                it.toModel()
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

    override suspend fun getOffers(userId: UUID): List<OfferClass> = newSuspendedTransaction {
        OfferEntity.find {
            OfferTable.client eq userId or (OfferTable.realtor eq userId)
        }.map { it.toModel() }
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
}