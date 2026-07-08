package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.theme.reset_theme

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.theme.ResetThemeEventSchema

object ResetThemeEventHolderBuilder : EventHolderBuilder<ResetThemeEventSchema, ResetThemeEventHolder> {

    override fun BuilderScope.build(
        eventSchema: ResetThemeEventSchema
    ) = with(eventSchema) {
        ResetThemeEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
