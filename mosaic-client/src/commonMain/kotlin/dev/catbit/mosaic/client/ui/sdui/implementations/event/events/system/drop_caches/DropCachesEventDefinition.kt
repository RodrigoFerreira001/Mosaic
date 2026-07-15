package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.drop_caches

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.system.DropCachesEventSchema

object DropCachesEventDefinition : EventDefinition<DropCachesEventSchema> {
    override val eventSchemaClass = DropCachesEventSchema::class
    override val eventRunner = DropCachesEventRunner
    override val eventHolderBuilder = DropCachesEventHolderBuilder
}
