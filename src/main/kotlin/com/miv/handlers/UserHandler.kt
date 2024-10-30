package com.miv.handlers

import com.miv.dto.ClientDTO
import com.miv.dto.RealtorDTO
import com.miv.models.user.Profile
import com.miv.models.user.Role

interface UserHandler {

    suspend fun search(query: String?, role: Role): List<Profile>

    suspend fun createClient(data: ClientDTO): Profile

    suspend fun createRealtor(data: RealtorDTO): Profile

    suspend fun updateRealtor(data: RealtorDTO, uuid: String): Profile

    suspend fun updateClient(data: ClientDTO, uuid: String): Profile

    suspend fun deleteUser(uuid: String)

    suspend fun getUserByID(id: String): Profile
}