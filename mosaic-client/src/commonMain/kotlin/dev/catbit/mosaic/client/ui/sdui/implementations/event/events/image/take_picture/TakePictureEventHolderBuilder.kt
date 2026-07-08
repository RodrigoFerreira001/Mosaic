package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.image.take_picture

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.image.TakePictureEventSchema

object TakePictureEventHolderBuilder : EventHolderBuilder<TakePictureEventSchema, TakePictureEventHolder> {

    override fun BuilderScope.build(
        eventSchema: TakePictureEventSchema
    ) = with(eventSchema) {
        TakePictureEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
