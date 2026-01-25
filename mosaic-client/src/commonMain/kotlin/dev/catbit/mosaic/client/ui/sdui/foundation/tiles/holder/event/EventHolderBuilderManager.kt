package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.core.data.event.EventModel
import kotlin.reflect.KClass

class EventHolderBuilderManager(
    private val builders: Map<KClass<out EventModel>, EventHolderBuilder<*, *>>
) {
    fun BuilderScope.build(eventModel: EventModel) = builders[eventModel::class]?.let { builder ->
        with(builder) { build(eventModel) }
    } ?: throw IllegalArgumentException("Couldn't find a builder for $eventModel")
}