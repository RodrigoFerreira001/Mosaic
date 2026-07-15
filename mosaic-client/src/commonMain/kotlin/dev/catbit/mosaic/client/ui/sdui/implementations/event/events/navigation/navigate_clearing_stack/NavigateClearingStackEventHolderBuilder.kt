package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_clearing_stack

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateClearingStackEventSchema

object NavigateClearingStackEventHolderBuilder : EventHolderBuilder<NavigateClearingStackEventSchema, NavigateClearingStackEventHolder> {

    override fun BuilderScope.build(
        eventSchema: NavigateClearingStackEventSchema
    ) = with(eventSchema) {
        NavigateClearingStackEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
