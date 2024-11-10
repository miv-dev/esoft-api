package com.miv.db.entities.event

import com.miv.db.tables.event.EventTable
import com.miv.models.event.Event
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime
import java.util.*

class EventEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EventEntity>(EventTable)

    var startAt by EventTable.startAt
    var endAt by EventTable.endAt
    var name by EventTable.name
    var comment by EventTable.comment
    var type by EventTable.type

    fun toModel() = Event(
        id = id.value,
        name = name ?: "Событие#${id.value.toString().substring(0, 4)}",
        startAt = startAt,
        eventType = type,
        endAt = endAt,
        comment = comment
    )
}