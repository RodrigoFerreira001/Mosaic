package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DismissDialogEventSchema

class DismissDialogEventHolder(
    override val id: String,
    override var event: DismissDialogEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<DismissDialogEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
