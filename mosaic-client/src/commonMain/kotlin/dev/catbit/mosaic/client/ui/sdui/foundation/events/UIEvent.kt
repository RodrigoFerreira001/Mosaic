package dev.catbit.mosaic.client.ui.sdui.foundation.events

import dev.catbit.mosaic.core.trigger.EventTrigger

sealed interface UIEvent {
    data class TriggerHolderUIEvent(
        val eventOwnerId: String,
        val trigger: EventTrigger
    ) : UIEvent

    data class TileEventHolderUIEvent(
        val tileId: String,
        val event: TileEvent
    ) : UIEvent
}