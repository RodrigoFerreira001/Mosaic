package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.change_screen_state

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.immutableMapTo

class ChangeScreenStateEventHolder(
    override val id: String,
    override var event: ChangeScreenStateEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<ChangeScreenStateEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
