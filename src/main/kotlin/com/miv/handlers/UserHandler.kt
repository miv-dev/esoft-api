package com.miv.handlers

import com.miv.models.Profile
import com.miv.models.Role

interface UserHandler {

    suspend fun search(query: String?, role: Role): List<Profile>

}