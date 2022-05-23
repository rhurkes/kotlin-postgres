@file:Suppress("unused")

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Serde {
    @ExperimentalSerializationApi
    @Suppress("ObjectPropertyName")
    val _instance: Json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @ExperimentalSerializationApi
    inline fun <reified T> encode(value: T) = _instance.encodeToString(value)

    @ExperimentalSerializationApi
    inline fun <reified T> decode(value: ByteArray) = _instance.decodeFromString<T>(String(value))

    @ExperimentalSerializationApi
    inline fun <reified T> decode(value: String) = _instance.decodeFromString<T>(value)
}

object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: ZonedDateTime) =
        encoder.encodeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))

    override fun deserialize(decoder: Decoder): ZonedDateTime = ZonedDateTime.parse(decoder.decodeString(), formatter)
}
