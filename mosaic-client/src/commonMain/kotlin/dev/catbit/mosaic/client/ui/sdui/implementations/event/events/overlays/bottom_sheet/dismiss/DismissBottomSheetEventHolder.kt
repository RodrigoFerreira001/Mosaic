package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.overlays.bottom_sheet.DismissBottomSheetEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class DismissBottomSheetEventHolder(
    override val id: String,
    override var event: DismissBottomSheetEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<DismissBottomSheetEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
