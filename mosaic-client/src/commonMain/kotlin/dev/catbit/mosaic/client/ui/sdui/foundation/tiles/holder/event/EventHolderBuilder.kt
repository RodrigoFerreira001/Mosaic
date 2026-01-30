package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.core.data.schemas.event.EventSchema

interface EventHolderBuilder<out T: EventSchema, E : EventHolder<@UnsafeVariance T>> {
    fun BuilderScope.build(eventSchema: @UnsafeVariance T): E
}
