package com.miv.services.impl

import com.miv.db.entities.UserEntity
import com.miv.models.user.Role
import com.miv.services.UserService
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*
import javax.inject.Inject

class UserServiceImpl @Inject constructor() : UserService {
    override suspend fun create(role: Role): UserEntity = newSuspendedTransaction {
        UserEntity.new {
            this.role = role
        }
    }

    override suspend fun getByID(id: UUID): UserEntity = newSuspendedTransaction{
        UserEntity[id]
    }

    override suspend fun delete(id: UUID) = newSuspendedTransaction {
        UserEntity[id].delete()
    }

    override suspend fun updateAvatar(id: UUID, avatar: String): UserEntity? {
        return newSuspendedTransaction {
            UserEntity.findByIdAndUpdate(id) {
                it.avatar = avatar
            }
        }
    }

}