package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.theme.set_color_mode

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetColorModeEventSchema

object SetColorModeEventHolderBuilder : EventHolderBuilder<SetColorModeEventSchema, SetColorModeEventHolder> {

    override fun BuilderScope.build(
        eventSchema: SetColorModeEventSchema
    ) = with(eventSchema) {
        SetColorModeEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
