package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.check_for_received_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.data.CheckForReceivedDataEventSchema

object CheckForReceivedDataEventHolderBuilder : EventHolderBuilder<CheckForReceivedDataEventSchema, CheckForReceivedDataEventHolder> {

    override fun BuilderScope.build(
        eventSchema: CheckForReceivedDataEventSchema
    ) = with(eventSchema) {
        CheckForReceivedDataEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
