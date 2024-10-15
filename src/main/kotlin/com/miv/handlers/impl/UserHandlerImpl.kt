package com.miv.handlers.impl

import com.miv.handlers.UserHandler
import com.miv.models.Profile
import com.miv.models.Role
import com.miv.services.ProfileService

class UserHandlerImpl(
    private val service: ProfileService
) : UserHandler {
    override suspend fun search(
        query: String?,
        role: Role
    ): List<Profile> {
        return when (role) {
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