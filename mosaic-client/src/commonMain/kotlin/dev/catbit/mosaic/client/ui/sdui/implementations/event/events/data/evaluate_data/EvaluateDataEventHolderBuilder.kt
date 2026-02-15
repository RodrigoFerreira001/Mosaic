package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.evaluate_data

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema

object EvaluateDataEventHolderBuilder : EventHolderBuilder<EvaluateDataEventSchema, EvaluateDataEventHolder> {

    override fun BuilderScope.build(
        eventSchema: EvaluateDataEventSchema
    ) = with(eventSchema) {
        EvaluateDataEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events?.map { eventModel -> buildEventHolder(eventModel) }
        )
    }
}
