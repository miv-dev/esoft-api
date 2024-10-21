package com.miv.services.impl

import com.miv.db.entities.ClientEntity
import com.miv.db.entities.RealtorEntity
import com.miv.db.tables.ClientProfileTable
import com.miv.db.tables.RealtorProfileTable
import com.miv.models.Profile
import com.miv.models.Role
import com.miv.services.SearchService
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.inject.Inject
import kotlin.math.min

class SearchServiceImpl @Inject constructor(): SearchService {
    override suspend fun searchUsers(query: String?, role: Role): List<Profile> = newSuspendedTransaction {
        when (role) {
            Role.ADMIN -> emptyList()
            Role.CLIENT -> ClientEntity.filter(
                query,
                USER_SEARCH_DISTANCE,
                ClientProfileTable.firstName,
                ClientProfileTable.lastName,
                ClientProfileTable.middleName
            ).map { it.toModel() }

            Role.REALTOR -> RealtorEntity.filter(
                query = query,
                USER_SEARCH_DISTANCE,
                RealtorProfileTable.firstName,
                RealtorProfileTable.lastName,
                RealtorProfileTable.middleName
            ).map { it.toModel() }
        }
    }

    private suspend fun <E : CompositeEntity, T : CompositeEntityClass<E>> T.filter(
        query: String?,
        distance: Int,
        vararg fields: Column<out String?>
    ): List<E> = newSuspendedTransaction {
        if (query.isNullOrBlank()) {
            this@filter.all().toList()
        } else {
            this@filter.all().filter { model ->
                var flag = false
                fields.forEach {
                    if (levenshtein(model.readValues[it], query) <= distance) flag = true
                }
                flag
            }
        }
    }

    private fun levenshtein(lhs: CharSequence?, rhs: CharSequence): Int {
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

    companion object {
        const val USER_SEARCH_DISTANCE = 3
    }
}