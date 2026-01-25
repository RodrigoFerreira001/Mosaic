package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.display

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.overlays.dialog.DisplayDialogEventModel

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
