package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.immutableMapTo

class ScreenTileHolder(
    override val id: String,
    override var tile: ScreenTileSchema,
    override val events: MutableList<EventHolder<*>>?,
    override val tiles: MutableList<TileHolder<*>>,
    private val navigationDrawerTiles: List<TileHolder<*>>?,
    private var currentBottomSheetTiles: List<TileHolder<*>>? = null,
    private var currentDialogSheetTiles: List<TileHolder<*>>? = null
) : TileHolder<ScreenTileSchema>() {

    override fun getTileHolder(
        tileId: String,
        includeEventsOnSearch: Boolean
    ): TileHolder<*>? =
        if (tileId == id) this
        else currentBottomSheetTiles?.firstNotNullOfOrNull { it.getTileHolder(tileId, includeEventsOnSearch) }
            ?: currentDialogSheetTiles?.firstNotNullOfOrNull { it.getTileHolder(tileId, includeEventsOnSearch) }
            ?: navigationDrawerTiles?.firstNotNullOfOrNull { it.getTileHolder(tileId, includeEventsOnSearch) }
            ?: tiles.firstNotNullOfOrNull { it.getTileHolder(tileId, includeEventsOnSearch) }
            ?: if (includeEventsOnSearch) events?.firstNotNullOfOrNull { it.getTileHolder(tileId) } else null

    override fun getEventHolder(eventId: String): EventHolder<*>? =
        currentBottomSheetTiles?.firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: currentDialogSheetTiles?.firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: navigationDrawerTiles?.firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: events?.firstNotNullOfOrNull { it.getEventHolder(eventId) }
            ?: tiles.firstNotNullOfOrNull { it.getEventHolder(eventId) }

    override fun getTileHoldersByGroupEvent(
        event: TileGroupEvent
    ): List<TileHolder<*>> = mutableListOf<TileHolder<*>>().apply {
        currentBottomSheetTiles.orEmpty().flatMap { it.getTileHoldersByGroupEvent(event) }.let(::addAll)
        currentDialogSheetTiles.orEmpty().flatMap { it.getTileHoldersByGroupEvent(event) }.let(::addAll)
        navigationDrawerTiles.orEmpty().flatMap { it.getTileHoldersByGroupEvent(event) }.let(::addAll)
        tiles.flatMap { it.getTileHoldersByGroupEvent(event) }.let(::addAll)
    }

    override fun getEventsByTrigger(
        eventTrigger: EventTrigger
    ): List<EventSchema>? = events
        ?.asSequence()
        ?.filter { it.trigger == eventTrigger }
        ?.map { it.get() }
        ?.plus(tiles.mapNotNull { it.getEventsByTrigger(eventTrigger) }.flatten())
        ?.plus(currentBottomSheetTiles?.mapNotNull { it.getEventsByTrigger(eventTrigger) }?.flatten().orEmpty())
        ?.plus(currentDialogSheetTiles?.mapNotNull { it.getEventsByTrigger(eventTrigger) }?.flatten().orEmpty())
        ?.plus(navigationDrawerTiles?.mapNotNull { it.getEventsByTrigger(eventTrigger) }?.flatten().orEmpty())
        ?.toList()

    override fun get() = tile.copy(
        tiles = tiles.immutableMapTo { it.get() },
        events = events?.immutableMapTo { it.get() },
        navigationDrawerTiles = navigationDrawerTiles?.immutableMapTo { it.get() },
        currentBottomSheetTiles = currentBottomSheetTiles?.immutableMapTo { it.get() },
        currentDialogSheetTiles = currentDialogSheetTiles?.immutableMapTo { it.get() },
    )

    fun setBottomSheetTiles(
        tiles: List<TileHolder<*>>
    ) {
        currentBottomSheetTiles = tiles
    }

    fun setDialogTiles(
        tiles: List<TileHolder<*>>
    ) {
        currentDialogSheetTiles = tiles
    }

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        if (event is ScreenTileEvents) {
            when (event) {
                ScreenTileEvents.OnCloseBottomSheetFinished -> {
                    currentBottomSheetTiles = null
                }

                ScreenTileEvents.OnCloseDialogFinished -> {
                    currentDialogSheetTiles = null
                }
            }
        }
    }
}