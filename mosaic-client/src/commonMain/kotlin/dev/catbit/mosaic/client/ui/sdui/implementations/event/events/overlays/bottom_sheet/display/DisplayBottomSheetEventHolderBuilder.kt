package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.display

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.overlays.bottom_sheet.DisplayBottomSheetEventModel

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
