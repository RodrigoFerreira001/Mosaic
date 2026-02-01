package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

class ScreenExtrasHolder {

    private val extras = mutableMapOf<String, Extra>()

    fun registerExtra(
        screenId: String,
        loadingTiles: List<TileSchema>,
        loadingEvents: List<EventSchema>,
        failureTiles: List<TileSchema>,
        failureEvents: List<EventSchema>,
    ) {
        extras[screenId] = Extra(
            loadingTiles = loadingTiles,
            loadingEvents = loadingEvents,
            failureTiles = failureTiles,
            failureEvents = failureEvents
        )
    }

    fun getExtra(screenId: String) = extras.getValue(screenId)

    fun removeExtra(screenId: String) {
        extras.remove(screenId)
    }

    data class Extra(
        val loadingTiles: List<TileSchema>,
        val loadingEvents: List<EventSchema>,
        val failureTiles: List<TileSchema>,
        val failureEvents: List<EventSchema>,
    )
}