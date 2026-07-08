package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.theme.set_theme

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetThemeEventSchema

object SetThemeEventHolderBuilder : EventHolderBuilder<SetThemeEventSchema, SetThemeEventHolder> {

    override fun BuilderScope.build(
        eventSchema: SetThemeEventSchema
    ) = with(eventSchema) {
        SetThemeEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
