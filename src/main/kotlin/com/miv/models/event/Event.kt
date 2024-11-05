package com.miv.models.event

import com.miv.utils.LocalDateTimeSerializer
import com.miv.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class Event(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("startAt")
    val startAt: LocalDateTime,
    val type: EventType,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("endAt")
    val endAt: LocalDateTime?,
    val comment: String?,
)
