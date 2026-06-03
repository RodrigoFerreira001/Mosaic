package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.snackbar.display

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DisplaySnackbarEventSchema

object DisplaySnackbarEventHolderBuilder : EventHolderBuilder<DisplaySnackbarEventSchema, DisplaySnackbarEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DisplaySnackbarEventSchema
    ) = with(eventSchema) {
        DisplaySnackbarEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
