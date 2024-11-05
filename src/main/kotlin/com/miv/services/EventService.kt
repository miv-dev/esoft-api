package com.miv.services

import com.miv.models.event.Event
import com.miv.models.event.EventType
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

interface EventService {
    suspend fun getAll(): List<Event>
    suspend fun getByID(id: UUID): Event
    suspend fun create(
        name: String?,
        startAt: LocalDateTime,
        endAt: LocalDateTime?,
        type: EventType,
        comment: String?
    ): Event

    suspend fun update(
        id: UUID,
        name: String?,
        startAt: LocalDateTime,
        endAt: LocalDateTime?,
        type: EventType,
        comment: String?
    ): Event

    suspend fun delete(id: UUID)
}