package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.data.event.EventModel

sealed interface UIEvent {
    data class EventModelHolderUIEvent(
        val events: List<EventModel>,
        val data: Any?
    ) : UIEvent

    data class TileEventHolderUIEvent(
        val tileId: String,
        val event: TileEvent
    ) : UIEvent
}