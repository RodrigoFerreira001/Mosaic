package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.check_for_received_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.data.CheckForReceivedDataEventModel

object CheckForReceivedDataEventHolderBuilder : EventHolderBuilder<CheckForReceivedDataEventModel, CheckForReceivedDataEventHolder> {

    override fun BuilderScope.build(
        eventModel: CheckForReceivedDataEventModel
    ) = with(eventModel) {
        CheckForReceivedDataEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
