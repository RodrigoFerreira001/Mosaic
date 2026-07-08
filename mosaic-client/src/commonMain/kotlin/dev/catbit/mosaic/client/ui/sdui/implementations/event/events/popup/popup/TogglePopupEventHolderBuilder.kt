package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.popup.popup

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.popup.TogglePopupEventSchema

object TogglePopupEventHolderBuilder : EventHolderBuilder<TogglePopupEventSchema, TogglePopupEventHolder> {

    override fun BuilderScope.build(
        eventSchema: TogglePopupEventSchema
    ) = with(eventSchema) {
        TogglePopupEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
