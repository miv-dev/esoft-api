package com.miv.services

import com.miv.models.Profile
import com.miv.models.Role

interface SearchService {
    suspend fun searchUsers(query: String?, role: Role): List<Profile>
}