package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.data.SendDataEventSchema

object SendDataEventHolderBuilder : EventHolderBuilder<SendDataEventSchema, SendDataEventHolder> {

    override fun BuilderScope.build(
        eventSchema: SendDataEventSchema
    ) = with(eventSchema) {
        SendDataEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
