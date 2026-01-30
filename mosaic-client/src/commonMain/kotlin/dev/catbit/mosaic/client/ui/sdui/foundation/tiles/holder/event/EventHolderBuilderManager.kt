package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import kotlin.reflect.KClass

class EventHolderBuilderManager(
    private val builders: Map<KClass<out EventSchema>, EventHolderBuilder<*, *>>
) {
    fun BuilderScope.build(eventSchema: EventSchema) = builders[eventSchema::class]?.let { builder ->
        with(builder) { build(eventSchema) }
    } ?: throw IllegalArgumentException("Couldn't find a builder for $eventSchema")
}