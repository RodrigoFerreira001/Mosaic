package dev.catbit.mosaic.core.serialization

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class MosaicSerializer(
    tileSerializers: Map<KClass<out TileSchema>, KSerializer<out TileSchema>>,
    eventSerializers: Map<KClass<out EventSchema>, KSerializer<out EventSchema>>,
    eventTriggerSerializers: Map<KClass<out EventTrigger>, KSerializer<out EventTrigger>> = mapOf()
) {

    @Suppress("UNCHECKED_CAST")
    private val json = Json {
        serializersModule = SerializersModule {
            polymorphic(TileSchema::class) {
                tileSerializers.forEach { (kClass, serializer) ->
                    subclass(
                        kClass as KClass<TileSchema>,
                        serializer as KSerializer<TileSchema>
                    )
                }
            }

            polymorphic(EventSchema::class) {
                eventSerializers.forEach { (kClass, serializer) ->
                    subclass(
                        kClass as KClass<EventSchema>,
                        serializer as KSerializer<EventSchema>
                    )
                }
            }

            polymorphic(EventTrigger::class) {
                (mapOf(
                    dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger::class to dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger.serializer(),
                    dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger::class to dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger.serializer(),
                    dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger::class to dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger.serializer(),
                    dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger::class to dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger.serializer(),
                    dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger::class to dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger.serializer(),
                    dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger::class to dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger.serializer(),
                    dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnMenuItemClickEventTrigger::class to dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnMenuItemClickEventTrigger.serializer()
                ) + eventTriggerSerializers).forEach { (kClass, serializer) ->
                    subclass(
                        kClass as KClass<EventTrigger>,
                        serializer as KSerializer<EventTrigger>
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