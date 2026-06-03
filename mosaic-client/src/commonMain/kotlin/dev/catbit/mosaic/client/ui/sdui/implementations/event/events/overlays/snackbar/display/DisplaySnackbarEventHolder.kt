package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.snackbar.display

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DisplaySnackbarEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

class DisplaySnackbarEventHolder(
    override val id: String,
    override var event: DisplaySnackbarEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<DisplaySnackbarEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
