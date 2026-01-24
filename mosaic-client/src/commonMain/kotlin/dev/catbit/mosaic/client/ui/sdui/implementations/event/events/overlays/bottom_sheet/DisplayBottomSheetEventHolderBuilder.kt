package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.overlays.DisplayBottomSheetEventModel

object DisplayBottomSheetEventHolderBuilder : EventHolderBuilder<DisplayBottomSheetEventModel, DisplayBottomSheetEventHolder> {

    override fun BuilderScope.build(
        eventModel: DisplayBottomSheetEventModel
    ) = with(eventModel) {
        DisplayBottomSheetEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) },
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }
        )
    }
}
