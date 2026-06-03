package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.snackbar.dismiss

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DismissSnackbarEventSchema

object DismissSnackbarEventHolderBuilder : EventHolderBuilder<DismissSnackbarEventSchema, DismissSnackbarEventHolder> {

    override fun BuilderScope.build(
        eventSchema: DismissSnackbarEventSchema
    ) = with(eventSchema) {
        DismissSnackbarEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
