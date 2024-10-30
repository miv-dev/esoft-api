package com.miv.services

import com.miv.db.entities.ClientEntity
import com.miv.db.entities.RealtorEntity
import com.miv.models.user.Profile
import com.miv.models.user.Role
import java.util.UUID

interface ProfileService {

    suspend fun createRealtor(
        firstName: String,
        lastName: String,
        middleName: String,
        dealShare: Double?
    ): RealtorEntity

    suspend fun createClient(
        firstName: String?,
        lastName: String?,
        middleName: String?,
        phone: String?,
        email: String?,
    ): ClientEntity

    suspend fun updateProfile(
        uuid: UUID,
        firstName: String?,
        lastName: String?,
        middleName: String?,
        phone: String?,
        email: String?,
    ): ClientEntity

    suspend fun updateProfile(
        uuid: UUID,
        firstName: String,
        lastName: String,
        middleName: String,
        dealShare: Double?
    ): RealtorEntity

    suspend fun getProfileByUserID(id: UUID): Profile

    suspend fun checkProfileExists(id: UUID, role: Role): Boolean
}