package com.miv.handlers.impl

import com.miv.dto.ClientDTO
import com.miv.dto.RealtorDTO
import com.miv.handlers.UserHandler
import com.miv.models.ClientProfile
import com.miv.models.Profile
import com.miv.models.RealtorProfile
import com.miv.models.Role
import com.miv.services.ProfileService
import com.miv.services.UserService
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*
import javax.inject.Inject

class UserHandlerImpl @Inject constructor(
    private val userService: UserService,
    private val profileService: ProfileService
) : UserHandler {
    override suspend fun search(
        query: String?,
        role: Role
    ): List<Profile> = newSuspendedTransaction {
        when (role) {
            Role.CLIENT -> {
                profileService.searchClients(query).map { it.toModel() }
            }

            Role.REALTOR -> {
                profileService.searchRealtors(query).map { it.toModel() }
            }

            else -> emptyList()
        }
    }

    override suspend fun createClient(data: ClientDTO): ClientProfile = newSuspendedTransaction {
        profileService.createClient(
            firstName = data.firstName,
            lastName = data.lastName,
            middleName = data.middleName,
            phone = data.phone,
            email = data.email,
        ).toModel()
    }

    override suspend fun createRealtor(data: RealtorDTO): RealtorProfile = newSuspendedTransaction {
        profileService.createRealtor(
            firstName = data.firstName,
            lastName = data.lastName,
            middleName = data.middleName,
            dealShare = data.dealShare
        ).toModel()
    }

    override suspend fun deleteUser(uuid: String) {
        userService.delete(UUID.fromString(uuid))
    }

    override suspend fun getUserByID(id: String): Profile {
        val uuid  = UUID.fromString(id)
        return profileService.getProfileByUserID(uuid)
    }

    override suspend fun updateRealtor(data: RealtorDTO, uuid: String): RealtorProfile = newSuspendedTransaction{
        val id = UUID.fromString(uuid)
        profileService.updateProfile(
            id,
            firstName = data.firstName,
            lastName = data.lastName,
            middleName = data.middleName,
            dealShare = data.dealShare
        ).toModel()
    }

    override suspend fun updateClient(data: ClientDTO, uuid: String): ClientProfile = newSuspendedTransaction {
        val id = UUID.fromString(uuid)
        profileService.updateProfile(
            id,
            firstName = data.firstName,
            lastName = data.lastName,
            middleName = data.middleName,
            phone = data.phone,
            email = data.email,
        ).toModel()
    }
}