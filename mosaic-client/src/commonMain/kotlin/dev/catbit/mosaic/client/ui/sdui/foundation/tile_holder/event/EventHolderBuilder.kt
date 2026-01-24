package dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.event

import dev.catbit.mosaic.client.ui.sdui.foundation.tile_holder.BuilderScope
import dev.catbit.mosaic.core.data.event.EventModel

interface EventHolderBuilder<out T: EventModel, E : EventHolder<@UnsafeVariance T>> {
    fun BuilderScope.build(eventModel: @UnsafeVariance T): E
}
