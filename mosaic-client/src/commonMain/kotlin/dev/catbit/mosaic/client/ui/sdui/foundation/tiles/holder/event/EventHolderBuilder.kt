package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.core.data.event.EventModel

interface EventHolderBuilder<out T: EventModel, E : EventHolder<@UnsafeVariance T>> {
    fun BuilderScope.build(eventModel: @UnsafeVariance T): E
}
