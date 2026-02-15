package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.evaluate_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger

class EvaluateDataEventHolder(
    override val id: String,
    override var event: EvaluateDataEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<EvaluateDataEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
