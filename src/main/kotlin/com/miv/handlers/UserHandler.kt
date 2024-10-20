package com.miv.handlers

import com.miv.dto.ClientDTO
import com.miv.dto.RealtorDTO
import com.miv.models.ClientProfile
import com.miv.models.Profile
import com.miv.models.RealtorProfile
import com.miv.models.Role

interface UserHandler {

    suspend fun search(query: String?, role: Role): List<Profile>

    suspend fun createClient(data: ClientDTO): ClientProfile

    suspend fun createRealtor(data: RealtorDTO): RealtorProfile

    suspend fun updateRealtor(data: RealtorDTO, uuid: String): RealtorProfile

    suspend fun updateClient(data: ClientDTO, uuid: String): ClientProfile

    suspend fun deleteUser(uuid: String)
}