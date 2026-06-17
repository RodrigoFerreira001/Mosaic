package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.check_if_has_internet_connection

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.events.system.CheckIfHasInternetConnectionEventSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo

class CheckIfHasInternetConnectionEventHolder(
    override val id: String,
    override var event: CheckIfHasInternetConnectionEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<CheckIfHasInternetConnectionEventSchema>() {

    override fun get() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
