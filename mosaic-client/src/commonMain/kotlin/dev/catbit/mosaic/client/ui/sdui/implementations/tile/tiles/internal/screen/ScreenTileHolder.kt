package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

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

    override fun getEventsByTrigger(
        eventTrigger: EventTrigger
    ): List<EventSchema>? = events
        ?.asSequence()
        ?.map { it.get() }
        ?.plus(tiles.mapNotNull { it.getEventsByTrigger(eventTrigger) }.flatten())
        ?.plus(currentBottomSheetTiles?.mapNotNull { it.getEventsByTrigger(eventTrigger) }?.flatten().orEmpty())
        ?.plus(currentDialogSheetTiles?.mapNotNull { it.getEventsByTrigger(eventTrigger) }?.flatten().orEmpty())
        ?.plus(navigationDrawerTiles?.mapNotNull { it.getEventsByTrigger(eventTrigger) }?.flatten().orEmpty())
        ?.toList()

    override fun get() = tile.copy(
        tiles = tiles.map { it.get() },
        events = events?.map { it.get() },
        navigationDrawerTiles = navigationDrawerTiles?.map { it.get() },
        currentBottomSheetTiles = currentBottomSheetTiles?.map { it.get() },
        currentDialogSheetTiles = currentDialogSheetTiles?.map { it.get() },
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

    override fun onTileEvent(event: TileEvent) {
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