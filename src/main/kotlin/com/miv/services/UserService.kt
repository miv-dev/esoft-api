package com.miv.services

import com.miv.db.entities.UserEntity
import com.miv.models.user.Role
import java.util.*

interface UserService {
    suspend fun create(role: Role): UserEntity
    suspend fun getByID(id: UUID): UserEntity
    suspend fun delete(id: UUID)

    suspend fun updateAvatar(id: UUID, avatar: String): UserEntity?
}
