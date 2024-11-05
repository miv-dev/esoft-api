package com.miv.services.impl

import com.miv.db.entities.event.EventEntity
import com.miv.models.event.Event
import com.miv.models.event.EventType
import com.miv.services.EventService
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class EventServiceImpl @Inject constructor() : EventService {

    override suspend fun getAll(): List<Event> = newSuspendedTransaction {
        EventEntity.all().map { it.toModel() }
    }

    override suspend fun getByID(id: UUID): Event = newSuspendedTransaction {
        EventEntity[id].toModel()
    }

    override suspend fun create(
        name: String?,
        startAt: LocalDateTime,
        endAt: LocalDateTime?,
        type: EventType,
        comment: String?
    ): Event = newSuspendedTransaction {
        EventEntity.new {
            this.name = name
            this.type = type
            this.comment = comment
            this.startAt = startAt
            this.endAt = endAt

        }.toModel()
    }

    override suspend fun update(
        id: UUID,
        name: String?,
        startAt: LocalDateTime,
        endAt: LocalDateTime?,
        type: EventType,
        comment: String?
    ): Event = newSuspendedTransaction {
        EventEntity.findByIdAndUpdate(id) {
            it.name = name
            it.type = type
            it.comment = comment
            it.startAt = startAt
            it.endAt = endAt
        }?.toModel() ?: throw NotFoundException("Event with id $id not found")
    }

    override suspend fun delete(id: UUID) = newSuspendedTransaction {
        EventEntity[id].delete()
    }
}