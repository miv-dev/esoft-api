package com.miv.services.impl

import com.miv.db.entities.deal.DealEntity
import com.miv.db.entities.demand.DemandEntity
import com.miv.db.entities.offer.OfferEntity
import com.miv.db.tables.deal.DealTable
import com.miv.db.tables.demand.DemandTable
import com.miv.db.tables.offer.OfferTable
import com.miv.models.deal.Deal
import com.miv.models.deal.DealClient
import com.miv.models.deal.DealObject
import com.miv.models.estate.Apartment
import com.miv.models.estate.House
import com.miv.models.estate.Land
import com.miv.services.DealService
import com.miv.services.DemandService
import com.miv.services.EstateService
import com.miv.services.OfferService
import io.ktor.server.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.inject.Inject
import kotlin.math.floor

class DealServiceImpl @Inject constructor(
    private val estateService: EstateService,
) : DealService {
    override suspend fun get(): List<Deal> = newSuspendedTransaction {
        DealEntity.all().map { it.toModel() }
    }


    override suspend fun create(offerID: UUID, demandID: UUID): Deal {
        isExist(demandID, offerID)

        return newSuspendedTransaction {
            DealEntity.new {
                demand = DemandEntity[demandID]
                offer = OfferEntity[offerID]
            }.toModel()
        }
    }

    override suspend fun delete(id: UUID) = newSuspendedTransaction {
        DealEntity[id].delete()
    }

    override suspend fun update(dealID: UUID, offerID: UUID, demandID: UUID): Deal = newSuspendedTransaction {
        isExist(dealID, demandID, offerID)

        DealEntity.findByIdAndUpdate(dealID) {
            it.demand = DemandEntity[demandID]
            it.offer = OfferEntity[offerID]
        }?.toModel() ?: throw EntityNotFoundException(EntityID(dealID, DealTable), DealEntity)

    }

    private suspend fun isExist(dealID: UUID, demandID: UUID, offerID: UUID) = newSuspendedTransaction {
        DealEntity.find(
            DealTable.id.neq(dealID) and
                    (DealTable.offer.eq(offerID) or DealTable.demand.eq(demandID))
        )
            .takeIf { it.count() == 0L }
            ?: throw BadRequestException("Deal with offer $offerID or demand $demandID already exists")
    }

    private suspend fun isExist(demandID: UUID, offerID: UUID) = newSuspendedTransaction {
        DealEntity.find(
            DealTable.offer.eq(offerID) or DealTable.demand.eq(demandID)
        )
            .takeIf { it.count() == 0L }
            ?: throw BadRequestException("Deal with offer $offerID or demand $demandID already exists")
    }




    private suspend fun DealEntity.toModel(): Deal {
        val estate = estateService.getByID(offer.estate.id.value)
        val sellerRealtorShare = offer.realtor.dealShare?.let { if (it > 1) it.div(100) else it } ?: 0.45
        val buyerRealtorShare = demand.realtor.dealShare?.let { if (it > 1) it.div(100) else it } ?: 0.45
        val sellerCost = when (estate.data) {
            is Apartment -> 36000 + 0.01 * offer.price
            is House -> 30000 + 0.01 * offer.price
            is Land -> 30000 + 0.02 * offer.price
        }
        val sellerCommission = sellerCost * sellerRealtorShare

        val buyerCost = offer.price * 0.03
        val buyerCommission = buyerCost * buyerRealtorShare


        return Deal(
            id = id.value,
            name = "Сделка#${id.value.toString().substring(0, 4)}",
            estate = DealObject(
                offer.price.toDouble(),
                offer.estate.type
            ),
            buyer = DealClient(
                floor(buyerCost),
                floor(buyerCommission)
            ),
            seller = DealClient(
                floor(sellerCost),
                floor(sellerCommission)
            ),
            companyDeduction = floor(sellerCost + buyerCost - buyerCommission - sellerCommission)
        )
    }
}