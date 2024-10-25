package com.miv.services.impl

import com.miv.db.entities.offer.OfferEntity
import com.miv.db.tables.offer.OfferTable
import com.miv.models.offer.Offer
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


    override suspend fun getOffers(): List<Offer> {
        return newSuspendedTransaction {
            OfferEntity.all().map {
                Offer(
                    id = it.id.value,
                    client = it.client.toModel(),
                    realtor = it.realtor.toModel(),
                    estate = estateService.getByID(it.estate.id.value),
                    price = it.price
                )
            }
        }
    }

    override suspend fun getOffer(id: UUID): Offer? {
        return newSuspendedTransaction {
            OfferEntity.findById(id)?.let {
                Offer(
                    id = id,
                    client = it.client.toModel(),
                    realtor = it.realtor.toModel(),
                    estate = estateService.getByID(it.estate.id.value),
                    price = it.price
                )
            }
        }
    }

    override suspend fun create(
        clientID: UUID,
        realtorID: UUID,
        realStateID: UUID,
        price: Int
    ): Offer {
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

    override suspend fun getOffers(userId: UUID): List<Offer> = newSuspendedTransaction {
        OfferEntity.find {
            OfferTable.client eq userId or (OfferTable.realtor eq userId)
        }.map {
            Offer(
                id = it.id.value,
                client = it.client.toModel(),
                realtor = it.realtor.toModel(),
                estate = estateService.getByID(it.estate.id.value),
                price = it.price
            )
        }
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
    ): Offer {
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

}