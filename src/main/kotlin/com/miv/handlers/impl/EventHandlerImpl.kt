package com.miv.handlers.impl

import com.miv.dto.EventDTO
import com.miv.handlers.EventHandler
import com.miv.models.event.Event
import com.miv.services.EventService
import io.ktor.server.plugins.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.inject.Inject

class EventHandlerImpl @Inject constructor(
    private val eventService: EventService
) : EventHandler {
    override suspend fun get(upcoming: Boolean): List<Event> {
        return if (upcoming) {
            eventService.getUpcomingEvents()
        } else {
            eventService.getAll()
        }
    }

    override suspend fun getOne(id: String): Event {
        val eventID = UUID.fromString(id)
        return eventService.getByID(eventID)
    }

    override suspend fun create(dto: EventDTO): Event {
        val startAt = LocalDateTime.of(dto.startDate, dto.startTime)
        val endAt = if (dto.endDate != null && dto.endTime != null) {
            LocalDateTime.of(dto.endDate, dto.endTime)
        } else null

        if (startAt < LocalDateTime.now()) {
            throw BadRequestException("Invalid start date")
        }
        endAt?.let {
            if (startAt > endAt) {
                throw BadRequestException("Invalid the end date should be after the start date")
            }
        }

        return eventService.create(
            dto.name,
            startAt,
            endAt,
            dto.type,
            dto.comment
        )
    }

    override suspend fun update(dto: EventDTO, id: String): Event {
        val startAt = LocalDateTime.of(dto.startDate, dto.startTime)
        val endAt = if (dto.endDate != null && dto.endTime != null) {
            LocalDateTime.of(dto.endDate, dto.endTime)
        } else null
        val eventID = UUID.fromString(id)

        return eventService.update(
            eventID,
            dto.name,
            startAt,
            endAt,
            dto.type,
            dto.comment
        )
    }

    override suspend fun delete(id: String) {
        val eventID = UUID.fromString(id)
        eventService.delete(eventID)
    }

    override suspend fun getGrouped(): Map<String, List<Event>> {
        return eventService.getGroupByDate()
    }
}