package com.miv.dto

import com.miv.models.event.EventType
import com.miv.utils.LocalDateSerializer
import com.miv.utils.LocalTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class EventDTO(
    val type: EventType,
    @SerialName("startDate")
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,

    @SerialName("startTime")
    @Serializable(with = LocalTimeSerializer::class)
    val startTime: LocalTime,

    @SerialName("endDate")
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate?,

    @SerialName("endTime")
    @Serializable(with = LocalTimeSerializer::class)
    val endTime: LocalTime?,

    val name: String?,
    val comment: String?,
)
