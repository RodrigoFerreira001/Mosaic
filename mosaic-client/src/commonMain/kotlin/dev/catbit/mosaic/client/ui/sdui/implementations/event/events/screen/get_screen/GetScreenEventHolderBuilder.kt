package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.get_screen

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema

object GetScreenEventHolderBuilder : EventHolderBuilder<GetScreenEventSchema, GetScreenEventHolder> {
    override fun BuilderScope.build(eventSchema: GetScreenEventSchema): GetScreenEventHolder =
        with(eventSchema) {
            GetScreenEventHolder(
                id = id,
                event = eventSchema,
                trigger = trigger,
                events = events.buildEventHolders()
            )
        }
}
