package com.miv.handlers

import com.miv.dto.ClientPost
import com.miv.dto.RealtorPost
import com.miv.models.ClientProfile
import com.miv.models.Profile
import com.miv.models.RealtorProfile
import com.miv.models.Role

interface UserHandler {

    suspend fun search(query: String?, role: Role): List<Profile>

    suspend fun createClient(data: ClientPost): ClientProfile

    suspend fun createRealtor(data: RealtorPost): RealtorProfile
}