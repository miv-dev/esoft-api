package com.miv.services.impl

import com.miv.db.entities.UserEntity
import com.miv.models.Role
import com.miv.services.UserService
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class UserServiceImpl : UserService {
    override suspend fun create(role: Role): UserEntity = newSuspendedTransaction {
        UserEntity.new {
            this.role = role
        }
    }

    override suspend fun delete(id: UUID) {
        UserEntity[id].delete()
    }
}