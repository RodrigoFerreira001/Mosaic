package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.modal_bottom_sheet.display

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DisplayModalBottomSheetEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class DisplayModalBottomSheetEventHolder(
    override val id: String,
    override var event: DisplayModalBottomSheetEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>
) : EventHolder<DisplayModalBottomSheetEventSchema>() {

    override fun getEventSchema() = event.copy(
        events = events?.immutableMapTo { it.get() },
        tiles = tiles.immutableMapTo { it.get() }
    )
}
