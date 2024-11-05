package com.miv.handlers

import com.miv.dto.EventDTO
import com.miv.models.event.Event

interface EventHandler {
    suspend fun getAll(): List<Event>
    suspend fun getOne(id: String): Event

    suspend fun create(dto: EventDTO): Event
    suspend fun update(dto: EventDTO, id: String): Event

    suspend fun delete(id: String)

}