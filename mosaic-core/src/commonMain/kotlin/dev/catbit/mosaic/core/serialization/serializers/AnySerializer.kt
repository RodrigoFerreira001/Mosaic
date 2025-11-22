@file:OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)

package dev.catbit.mosaic.core.serialization.serializers

import dev.catbit.mosaic.core.extensions.toAny
import dev.catbit.mosaic.core.extensions.toJsonElement
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

object AnySerializer : KSerializer<Any> {

    override val descriptor = buildClassSerialDescriptor("Any")

    override fun serialize(encoder: Encoder, value: Any) {
        encoder.encodeSerializableValue(
            serializer = JsonElement.serializer(),
            value = value.toJsonElement()
        )
    }

    override fun deserialize(decoder: Decoder): Any =
        (decoder as JsonDecoder).decodeJsonElement().toAny()
            ?: throw SerializationException("Unexpected null value for non-nullable Any type")
}

typealias AnySerializable = @Serializable(with = AnySerializer::class) Any