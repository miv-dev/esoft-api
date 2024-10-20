package com.miv.services

import com.miv.db.entities.ClientEntity
import com.miv.db.entities.RealtorEntity
import com.miv.models.Profile
import com.miv.models.Role
import com.miv.models.User
import jdk.jfr.Percentage
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


    suspend fun searchClients(query: String?): List<ClientEntity>

    suspend fun searchRealtors(query: String?): List<RealtorEntity>

}