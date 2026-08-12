package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.modal_bottom_sheet.display

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DisplayModalBottomSheetEventSchema

object DisplayModalBottomSheetEventHolderBuilder : EventHolderBuilder<DisplayModalBottomSheetEventSchema, DisplayModalBottomSheetEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DisplayModalBottomSheetEventSchema
    ) = with(eventSchema) {
        DisplayModalBottomSheetEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders(),
            tiles = tiles.buildTileHolders()
        )
    }
}
