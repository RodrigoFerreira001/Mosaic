package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.image.get_image_from_galery

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.image.GetImageFromGalleryEventSchema

object GetImageFromGalleryEventDefinition : EventDefinition<GetImageFromGalleryEventSchema> {
    override val eventSchemaClass = GetImageFromGalleryEventSchema::class
    override val eventRunner = GetImageFromGalleryEventRunner
    override val eventHolderBuilder = GetImageFromGalleryEventHolderBuilder
}
