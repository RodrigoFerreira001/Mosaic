package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.security.request_permission

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.security.RequestPermissionEventModel

object RequestPermissionEventHolderBuilder : EventHolderBuilder<RequestPermissionEventModel, RequestPermissionEventHolder> {

    override fun BuilderScope.build(
        eventModel: RequestPermissionEventModel
    ) = with(eventModel) {
        RequestPermissionEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
