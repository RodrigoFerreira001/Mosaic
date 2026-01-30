package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.display

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DisplayDialogEventSchema

object DisplayDialogEventHolderBuilder : EventHolderBuilder<DisplayDialogEventSchema, DisplayDialogEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DisplayDialogEventSchema
    ) = with(eventSchema) {
        DisplayDialogEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) },
            tiles = tiles.map { tileModel -> buildTileHolder(tileModel) }
        )
    }
}
