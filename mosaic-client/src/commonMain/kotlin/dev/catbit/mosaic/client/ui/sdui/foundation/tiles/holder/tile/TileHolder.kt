package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.UpdateScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.extensions.runSafely
import dev.catbit.mosaic.core.extensions.toJsonElement
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer

abstract class TileHolder<T : TileSchema> {
    abstract val id: String
    protected abstract var tile: T

    protected abstract val events: MutableList<EventHolder<*>>?
    protected abstract val tiles: MutableList<TileHolder<*>>?

    open fun getTileHolder(
        tileId: String,
        includeEventsOnSearch: Boolean = false
    ): TileHolder<*>? =
        if (tileId == id) this
        else tiles?.firstNotNullOfOrNull { it.getTileHolder(tileId, includeEventsOnSearch) }
            ?: if (includeEventsOnSearch) events?.firstNotNullOfOrNull { it.getTileHolder(tileId) } else null

    open fun getTileHoldersByGroupEvent(
        event: TileGroupEvent
    ): List<TileHolder<*>> = mutableListOf<TileHolder<*>>().apply {
        if (handlesGroupEvent(event)) add(this@TileHolder)
        tiles?.flatMap { it.getTileHoldersByGroupEvent(event) }?.let(::addAll)
    }

    open fun handlesGroupEvent(event: TileGroupEvent): Boolean = false

    open fun getEventHolder(eventId: String): EventHolder<*>? =
        events?.firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: tiles?.firstNotNullOfOrNull { it.getEventHolder(eventId) }

    open fun getEventsByTrigger(
        eventTrigger: EventTrigger
    ): List<EventSchema>? = events
        ?.filter { it.trigger == eventTrigger }
        ?.map { it.get() }
        ?.plus(tiles?.mapNotNull { it.getEventsByTrigger(eventTrigger) }?.flatten().orEmpty())

    abstract fun get(): T

    @OptIn(InternalSerializationApi::class)
    fun UpdateScope.update(updateData: Map<String, Any?>) {
        runSafely {

            val tileUpdateObject = updateData
                .filterKeys { it != "style" }
                .toJsonElement()
                .jsonObject

            val styleUpdateObject = updateData["style"]
                ?.toJsonElement()
                ?.jsonObject

            val tileObject = tile.toJsonElement().jsonObject
            val styleObject = tile.style.toJsonElement().jsonObject

            val updatedStyleObject = styleUpdateObject?.let {
                buildJsonObject {
                    styleObject.forEach { (key, element) -> put(key, element) }
                    styleUpdateObject.forEach { (key, element) -> put(key, element) }
                }
            }

            val updatedObject = buildJsonObject {
                tileObject.forEach { (key, element) -> put(key, element) }
                tileUpdateObject.forEach { (key, element) -> put(key, element) }
                updatedStyleObject?.let { put("style", updatedStyleObject) }
            }

            tile = serializer.decodeFromJsonElement(
                deserializer = tile::class.serializer(),
                element = updatedObject
            )
        }
    }

    open fun onTileEvent(event: TileEvent) = Unit

    open fun onTileGroupEvent(event: TileGroupEvent) = Unit

    fun addChild(
        child: TileHolder<*>,
        where: InsertionPosition = InsertionPosition.End
    ) {
        tiles?.add(
            element = child,
            index = where.toIndex(tiles.orEmpty())
        )
    }

    fun addChildren(
        children: List<TileHolder<*>>,
        where: InsertionPosition = InsertionPosition.End
    ) {
        tiles?.addAll(
            elements = children,
            index = where.toIndex(tiles.orEmpty())
        )
    }

    fun removeChild(id: String) {
        tiles?.removeAll { it.id == id }
    }

    fun removeChildren(ids: List<String>) {
        tiles?.removeAll { it.id in ids }
    }

    fun wipeChildren() {
        tiles?.clear()
    }
}