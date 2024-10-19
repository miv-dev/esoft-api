package com.miv.handlers.impl

import com.miv.dto.ClientPost
import com.miv.dto.RealtorPost
import com.miv.handlers.UserHandler
import com.miv.models.ClientProfile
import com.miv.models.Profile
import com.miv.models.RealtorProfile
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

    override suspend fun createClient(data: ClientPost): ClientProfile = newSuspendedTransaction {
        service.createClient(
            firstName = data.firstName,
            lastName = data.lastName,
            middleName = data.middleName,
            phone = data.phone,
            email = data.email,
        ).toModel()
    }

    override suspend fun createRealtor(data: RealtorPost): RealtorProfile = newSuspendedTransaction {
        service.createRealtor(
            firstName = data.firstName,
            lastName = data.lastName,
            middleName = data.middleName,
            dealShare = data.dealShare
        ).toModel()
    }
}