package com.miv.services

import com.miv.models.user.Profile
import com.miv.models.user.Role

interface SearchService {
    suspend fun searchUsers(query: String?, role: Role): List<Profile>
}