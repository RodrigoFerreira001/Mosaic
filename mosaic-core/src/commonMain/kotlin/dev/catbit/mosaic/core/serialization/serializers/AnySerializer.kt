@file:OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)

package dev.catbit.mosaic.core.serialization.serializers

import dev.catbit.mosaic.core.extensions.toAny
import dev.catbit.mosaic.core.extensions.toJsonElement
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder

/**
 * Serializes an untyped [Any] value by converting it to/from a raw [JsonElement] tree — via
 * `Any?.toJsonElement()`/`JsonElement.toAny()` — the mechanism behind every "opaque payload" field in
 * the framework (an event's `body`, `data`, `EvaluateData`'s inline literals, etc.), where the field's
 * real shape is only known at runtime, not at schema-authoring time. Reached via the [AnySerializable]
 * typealias, never used directly.
 *
 * When the active [Encoder]/[Decoder] is JSON-native ([JsonEncoder]/[JsonDecoder] — true for
 * [dev.catbit.mosaic.core.serialization.MosaicSerializer.json], the format the whole framework
 * actually uses), the tree is embedded structurally. For a non-JSON format, it falls back to
 * embedding the JSON tree as a single encoded string — a degraded but still round-trippable path,
 * never exercised by the framework's own JSON-only wire format in practice.
 */
object AnySerializer : KSerializer<Any> {

    override val descriptor = buildClassSerialDescriptor("Any")

    override fun serialize(encoder: Encoder, value: Any) {
        if (encoder is JsonEncoder) {
            encoder.encodeSerializableValue(
                serializer = JsonElement.serializer(),
                value = value.toJsonElement(encoder.json)
            )
        } else {
            encoder.encodeString(Json.encodeToString(JsonElement.serializer(), value.toJsonElement(Json)))
        }
    }

    override fun deserialize(decoder: Decoder): Any =
        (
            if (decoder is JsonDecoder) {
                decoder.decodeJsonElement()
            } else {
                Json.decodeFromString(JsonElement.serializer(), decoder.decodeString())
            }
        ).toAny() ?: throw SerializationException("Unexpected null value for non-nullable Any type")
}

/** `Any` pre-wired to [AnySerializer] — every "opaque payload" field in a schema (an event's `body`,
 * `data`, etc.) is declared with this typealias (or its nullable form, `AnySerializable?`) rather
 * than a plain `Any`, so `kotlinx.serialization` knows how to (de)serialize a value whose real shape
 * is only known at runtime. */
typealias AnySerializable = @Serializable(with = AnySerializer::class) Any