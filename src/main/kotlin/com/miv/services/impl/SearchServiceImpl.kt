package com.miv.services.impl

import com.miv.db.entities.ClientEntity
import com.miv.db.entities.RealtorEntity
import com.miv.db.tables.ClientProfileTable
import com.miv.db.tables.RealtorProfileTable
import com.miv.models.Profile
import com.miv.models.Role
import com.miv.services.SearchService
import com.miv.utils.levenshtein
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.inject.Inject
import kotlin.math.min

class SearchServiceImpl @Inject constructor() : SearchService {
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

    private suspend fun <E : UUIDEntity, T : UUIDEntityClass<E>> T.filter(
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


    companion object {
        const val USER_SEARCH_DISTANCE = 3
    }
}