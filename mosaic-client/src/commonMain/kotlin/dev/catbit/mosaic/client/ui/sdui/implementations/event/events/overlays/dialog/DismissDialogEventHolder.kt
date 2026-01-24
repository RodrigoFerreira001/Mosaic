package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.overlays.DismissDialogEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class DismissDialogEventHolder(
    override val id: String,
    override var event: DismissDialogEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<DismissDialogEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
