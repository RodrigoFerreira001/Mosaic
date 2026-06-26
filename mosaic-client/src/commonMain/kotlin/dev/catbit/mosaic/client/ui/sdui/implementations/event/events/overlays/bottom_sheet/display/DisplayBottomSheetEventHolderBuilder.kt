package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.display

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DisplayBottomSheetEventSchema

object DisplayBottomSheetEventHolderBuilder : EventHolderBuilder<DisplayBottomSheetEventSchema, DisplayBottomSheetEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DisplayBottomSheetEventSchema
    ) = with(eventSchema) {
        DisplayBottomSheetEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
