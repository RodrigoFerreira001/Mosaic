package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.event.events.event.TriggerEventEventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger

class TriggerEventEventHolder(
    override val id: String,
    override var event: TriggerEventEventModel,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<TriggerEventEventModel>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
