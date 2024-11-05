package com.miv.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("time", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_TIME)
    }

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(DateTimeFormatter.ISO_TIME.format(value))
    }

}