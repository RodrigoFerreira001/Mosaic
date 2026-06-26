package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.transform_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.data.TransformDataEventSchema

object TransformDataEventHolderBuilder : EventHolderBuilder<TransformDataEventSchema, TransformDataEventHolder> {

    override fun BuilderScope.build(
        eventSchema: TransformDataEventSchema
    ) = with(eventSchema) {
        TransformDataEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
