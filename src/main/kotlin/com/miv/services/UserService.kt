package com.miv.services

import com.miv.db.entities.UserEntity
import com.miv.models.Role
import com.miv.models.User
import java.util.*

interface UserService {
    suspend fun create(role: Role): UserEntity
    suspend fun getByID(id: UUID): UserEntity
    suspend fun delete(id: UUID)
}
