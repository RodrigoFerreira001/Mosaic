package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.UpdateScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.runSafely
import dev.catbit.mosaic.core.extensions.toJsonElement
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import kotlin.collections.component1
import kotlin.collections.component2

abstract class EventHolder<T : EventSchema> {
    abstract val id: String
    protected abstract var event: T
    abstract val trigger: EventTrigger

    protected abstract val events: List<EventHolder<*>>?
    protected abstract val tiles: List<TileHolder<*>>?

    fun getEventHolder(eventId: String): EventHolder<*>? =
        if (eventId == id) this
        else events?.firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: tiles?.firstNotNullOfOrNull { it.getEventHolder(eventId) }

    fun getTileHolder(tileId: String): TileHolder<*>? =
        tiles?.firstNotNullOfOrNull { it.getTileHolder(tileId, true) }
            ?: events?.firstNotNullOfOrNull { it.getTileHolder(tileId) }

    abstract fun get(): T

    @OptIn(InternalSerializationApi::class)
    fun UpdateScope.update(updateData: Map<String, Any?>) {

        // TODO Maybe, just maybe, change this part to an event update with new events or tiles, can update the current tile and event holders

        runSafely {
            val updateObject = updateData.toJsonElement().jsonObject
            val eventObject = serializer.encodeEventToJsonElement(event).jsonObject

            val updatedObject = buildJsonObject {
                eventObject.forEach { (key, element) -> put(key, element) }
                updateObject.forEach { (key, element) -> put(key, element) }
            }

            event = serializer.decodeFromJsonElement(
                deserializer = event::class.serializer(),
                element = updatedObject
            )
        }
    }
}