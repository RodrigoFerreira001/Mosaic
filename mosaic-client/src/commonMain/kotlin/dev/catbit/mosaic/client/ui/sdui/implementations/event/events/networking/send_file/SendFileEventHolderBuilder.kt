package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.send_file

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendFileEventSchema

object SendFileEventHolderBuilder : EventHolderBuilder<SendFileEventSchema, SendFileEventHolder> {

    override fun BuilderScope.build(
        eventSchema: SendFileEventSchema
    ) = with(eventSchema) {
        SendFileEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
