package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema

interface TilesEventDispatcher {
    fun onEvent(
        tileId: String,
        event: TileEvent,
        onError: (Throwable) -> Unit = {},
        onSuccess: () -> Unit = {}
    )

    fun onGroupEvent(
        event: TileGroupEvent,
        onError: (Throwable) -> Unit = {},
        onSuccess: () -> Unit = {}
    )

    fun getEventSchema(eventId: String): EventSchema?

    fun updateEventHolder(
        eventId: String,
        data: Map<String, Any?>,
        onError: (Throwable) -> Unit = {},
        onSuccess: () -> Unit = {}
    )
}