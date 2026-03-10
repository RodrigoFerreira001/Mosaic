package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_countdown_timer

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartCountdownTimerEventSchema

class StartCountdownTimerEventHolder(
    override val id: String,
    override var event: StartCountdownTimerEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<StartCountdownTimerEventSchema>() {

    override fun get() = event.copy(
        events = events?.map { it.get() }
    )
}
