package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.event.events.data.ProcessDataEventModel

object ProcessDataEventHolderBuilder : EventHolderBuilder<ProcessDataEventModel, ProcessDataEventHolder> {

    override fun BuilderScope.build(
        eventModel: ProcessDataEventModel
    ) = with(eventModel) {
        ProcessDataEventHolder(
            id = id,
            event = eventModel,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
