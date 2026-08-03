package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.open_external_link

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.core.data.schemas.event.events.system.OpenExternalLinkEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.immutableMapTo

class OpenExternalLinkEventHolder(
    override val id: String,
    override var event: OpenExternalLinkEventSchema,
    override val trigger: EventTrigger,
    override val events: List<EventHolder<*>>?,
    override val tiles: List<TileHolder<*>>? = null
) : EventHolder<OpenExternalLinkEventSchema>() {

    override fun getEventSchema() = event.copy(
        events = events?.immutableMapTo { it.get() }
    )
}
