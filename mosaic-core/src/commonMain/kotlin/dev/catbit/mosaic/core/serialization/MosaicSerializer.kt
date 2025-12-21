package dev.catbit.mosaic.core.serialization

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class MosaicSerializer(
    tileSerializers: Map<KClass<out TileModel>, KSerializer<out TileModel>>,
    eventSerializers: Map<KClass<out EventModel>, KSerializer<out EventModel>>
) {

    @Suppress("UNCHECKED_CAST")
    private val json = Json {
        serializersModule = SerializersModule {
            polymorphic(TileModel::class) {
                tileSerializers.forEach { (kClass, serializer) ->
                    subclass(
                        kClass as KClass<TileModel>,
                        serializer as KSerializer<TileModel>
                    )
                }
            }

            polymorphic(EventModel::class) {
                eventSerializers.forEach { (kClass, serializer) ->
                    subclass(
                        kClass as KClass<EventModel>,
                        serializer as KSerializer<EventModel>
                    )
                }
            }
        }
        explicitNulls = false
        encodeDefaults = true
    }

    fun <T> encodeToString(
        serializer: SerializationStrategy<T>,
        value: T
    ): String = json.encodeToString(serializer, value)

    fun <T> decodeFromString(
        deserializer: DeserializationStrategy<T>,
        string: String
    ): T = json.decodeFromString(deserializer, string)

    fun <T> encodeToJsonElement(
        serializer: SerializationStrategy<T>,
        value: T
    ): JsonElement = json.encodeToJsonElement(serializer, value)

    fun <T> decodeFromJsonElement(
        deserializer: DeserializationStrategy<T>,
        element: JsonElement
    ): T = json.decodeFromJsonElement(deserializer, element)

    fun parseToJsonElement(
        string: String
    ): JsonElement = json.parseToJsonElement(string)

    inline fun <reified T> encodeToString(
        value: T
    ): String = encodeToString(serializer(), value)

    inline fun <reified T> decodeFromString(
        string: String
    ): T = decodeFromString(serializer(), string)
}