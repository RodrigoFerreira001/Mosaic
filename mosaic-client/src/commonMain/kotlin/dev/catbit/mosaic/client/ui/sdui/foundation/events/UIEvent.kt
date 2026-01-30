package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.data.schemas.event.EventSchema

sealed interface UIEvent {
    data class EventSchemaHolderUIEvent(
        val events: List<EventSchema>,
        val data: Any?
    ) : UIEvent

    data class TileEventHolderUIEvent(
        val tileId: String,
        val event: TileEvent
    ) : UIEvent
}