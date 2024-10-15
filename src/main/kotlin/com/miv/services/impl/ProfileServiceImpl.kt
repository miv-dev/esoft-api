package com.miv.services.impl

import com.miv.db.entities.ClientEntity
import com.miv.db.entities.RealtorEntity
import com.miv.models.Role
import com.miv.services.ProfileService
import com.miv.services.UserService
import io.ktor.server.plugins.BadRequestException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.inject.Inject
import kotlin.math.min

class ProfileServiceImpl @Inject constructor(
    private val userService: UserService,
) : ProfileService {
    override suspend fun createRealtor(
        firstName: String,
        lastName: String,
        middleName: String,
        feePercentage: Double?
    ): RealtorEntity = newSuspendedTransaction {

        val user = userService.create(Role.REALTOR)

        RealtorEntity.new {
            this.user = user
            this.firstName = firstName
            this.lastName = lastName
            this.middleName = middleName
            this.feePercentage = feePercentage
        }
    }

    override suspend fun createClient(
        firstName: String?,
        lastName: String?,
        middleName: String?,
        phone: String?,
        email: String?
    ): ClientEntity = newSuspendedTransaction {
        val user = userService.create(Role.CLIENT)

        ClientEntity.new {
            this.user = user
            this.firstName = firstName
            this.lastName = lastName
            this.middleName = middleName
            this.phone = phone
            this.email = email
        }
    }


    override suspend fun searchClients(query: String?): List<ClientEntity> {
        if (query == null) {
            return ClientEntity.all().toList()
        }

        return ClientEntity.all().filter {
            levenshtein(it.firstName, query) <= MAX_DISTANCE ||
                    levenshtein(it.lastName, query) <= MAX_DISTANCE ||
                    levenshtein(it.middleName, query) <= MAX_DISTANCE
        }
    }

    override suspend fun searchRealtors(query: String?): List<RealtorEntity> {
        if (query == null) {
            return RealtorEntity.all().toList()
        }

        return RealtorEntity.all().filter {
            levenshtein(it.firstName, query) <= MAX_DISTANCE ||
                    levenshtein(it.lastName, query) <= MAX_DISTANCE ||
                    levenshtein(it.middleName, query) <= MAX_DISTANCE
        }
    }


    companion object {
        const val MAX_DISTANCE = 3
    }
}


fun levenshtein(lhs: CharSequence?, rhs: CharSequence): Int {
    when {
        lhs == null -> return rhs.length
        lhs == rhs -> return 0
        lhs.isEmpty() -> return rhs.length
        rhs.isEmpty() -> return lhs.length
    }
    val lhsLength = lhs!!.length + 1
    val rhsLength = rhs.length + 1

    var cost = Array(lhsLength) { it }
    var newCost = Array(lhsLength) { 0 }

    for (i in 1..<rhsLength) {
        newCost[0] = i

        for (j in 1..<lhsLength) {
            val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1

            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1

            newCost[j] = min(min(costInsert, costDelete), costReplace)
        }

        val swap = cost
        cost = newCost
        newCost = swap
    }

    return cost[lhsLength - 1]
}