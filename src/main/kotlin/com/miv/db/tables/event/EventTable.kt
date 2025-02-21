package com.miv.db.tables.event

import com.miv.models.event.EventType
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object EventTable : UUIDTable("events") {
    val type = enumeration<EventType>("type")
    val startAt = datetime("start_at")
    val endAt = datetime("end_at").nullable()
    val name = varchar("name", 100).nullable()
    val comment = text("comment").nullable()
}
