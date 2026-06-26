package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.check_if_has_internet_connection

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.system.CheckIfHasInternetConnectionEventSchema

object CheckIfHasInternetConnectionEventHolderBuilder : EventHolderBuilder<CheckIfHasInternetConnectionEventSchema, CheckIfHasInternetConnectionEventHolder> {

    override fun BuilderScope.build(
        eventSchema: CheckIfHasInternetConnectionEventSchema
    ) = with(eventSchema) {
        CheckIfHasInternetConnectionEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
