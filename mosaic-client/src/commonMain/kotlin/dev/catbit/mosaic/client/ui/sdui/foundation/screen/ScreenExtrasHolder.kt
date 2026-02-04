package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

class ScreenExtrasHolder {

    private val extras = mutableMapOf<String, Extra>()

    fun registerExtra(
        screenId: String,
        initialTiles: List<TileSchema>,
        initialEvents: List<EventSchema>,
        failureTiles: List<TileSchema>,
        failureEvents: List<EventSchema>,
    ) {
        extras[screenId] = Extra(
            initialTiles = initialTiles,
            initialEvents = initialEvents,
            failureTiles = failureTiles,
            failureEvents = failureEvents
        )
    }

    fun getExtra(screenId: String) = extras.getValue(screenId)

    fun removeExtra(screenId: String) {
        extras.remove(screenId)
    }

    data class Extra(
        val initialTiles: List<TileSchema>,
        val initialEvents: List<EventSchema>,
        val failureTiles: List<TileSchema>,
        val failureEvents: List<EventSchema>,
    )
}