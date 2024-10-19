package com.miv.handlers.impl

import com.miv.handlers.UserHandler
import com.miv.models.Profile
import com.miv.models.Role
import com.miv.services.ProfileService
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.inject.Inject

class UserHandlerImpl @Inject constructor(
    private val service: ProfileService
) : UserHandler {
    override suspend fun search(
        query: String?,
        role: Role
    ): List<Profile> = newSuspendedTransaction {
         when (role) {
            Role.CLIENT -> {
                service.searchClients(query).map { it.toModel() }
            }

            Role.REALTOR -> {
                service.searchRealtors(query).map { it.toModel() }
            }

            else -> emptyList()
        }
    }
}