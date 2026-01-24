package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.overlays.DisplayDialogEventModel

object DisplayDialogEventHolderBuilder : EventHolderBuilder<DisplayDialogEventModel, DisplayDialogEventHolder> {

    override fun BuilderScope.build(
        eventModel: DisplayDialogEventModel
    ) = with(eventModel) {
        DisplayDialogEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) },
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }
        )
    }
}
