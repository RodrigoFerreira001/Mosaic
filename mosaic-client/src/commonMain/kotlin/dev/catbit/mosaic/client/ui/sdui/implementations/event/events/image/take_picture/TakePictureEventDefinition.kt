package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.image.take_picture

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.image.TakePictureEventSchema

object TakePictureEventDefinition : EventDefinition<TakePictureEventSchema> {
    override val eventSchemaClass = TakePictureEventSchema::class
    override val eventRunner = TakePictureEventRunner
    override val eventHolderBuilder = TakePictureEventHolderBuilder
}
