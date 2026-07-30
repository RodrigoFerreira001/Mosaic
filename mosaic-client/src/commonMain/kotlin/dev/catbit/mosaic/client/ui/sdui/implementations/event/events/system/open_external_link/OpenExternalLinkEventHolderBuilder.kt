package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.open_external_link

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.system.OpenExternalLinkEventSchema

object OpenExternalLinkEventHolderBuilder :
    EventHolderBuilder<OpenExternalLinkEventSchema, OpenExternalLinkEventHolder> {

    override fun BuilderScope.build(
        eventSchema: OpenExternalLinkEventSchema
    ) = with(eventSchema) {
        OpenExternalLinkEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
