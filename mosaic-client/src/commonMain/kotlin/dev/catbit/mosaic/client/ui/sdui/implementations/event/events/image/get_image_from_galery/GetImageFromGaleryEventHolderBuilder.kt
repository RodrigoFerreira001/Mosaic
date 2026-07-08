package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.image.get_image_from_galery

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.image.GetImageFromGalleryEventSchema

object GetImageFromGalleryEventHolderBuilder :
    EventHolderBuilder<GetImageFromGalleryEventSchema, GetImageFromGalleryEventHolder> {

    override fun BuilderScope.build(
        eventSchema: GetImageFromGalleryEventSchema
    ) = with(eventSchema) {
        GetImageFromGalleryEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
