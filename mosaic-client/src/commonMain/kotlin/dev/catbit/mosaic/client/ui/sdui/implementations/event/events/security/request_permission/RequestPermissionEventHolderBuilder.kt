package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.security.request_permission

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema

object RequestPermissionEventHolderBuilder : EventHolderBuilder<RequestPermissionEventSchema, RequestPermissionEventHolder> {

    override fun BuilderScope.build(
        eventSchema: RequestPermissionEventSchema
    ) = with(eventSchema) {
        RequestPermissionEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
