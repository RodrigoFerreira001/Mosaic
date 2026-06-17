package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.broadcast_to_system

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.system.BroadcastToSystemEventSchema

object BroadcastToSystemEventDefinition : EventDefinition<BroadcastToSystemEventSchema> {
    override val eventSchemaClass = BroadcastToSystemEventSchema::class
    override val eventRunner = BroadcastToSystemEventRunner
    override val eventHolderBuilder = BroadcastToSystemEventHolderBuilder
}
