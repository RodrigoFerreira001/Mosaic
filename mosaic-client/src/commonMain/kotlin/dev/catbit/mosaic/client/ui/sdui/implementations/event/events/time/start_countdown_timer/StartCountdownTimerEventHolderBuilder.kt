package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_countdown_timer

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartCountdownTimerEventSchema

object StartCountdownTimerEventHolderBuilder : EventHolderBuilder<StartCountdownTimerEventSchema, StartCountdownTimerEventHolder> {

    override fun BuilderScope.build(
        eventSchema: StartCountdownTimerEventSchema
    ) = with(eventSchema) {
        StartCountdownTimerEventHolder(
            id = id,
            event = eventSchema,
            trigger = trigger,
            events = events.buildEventHolders()
        )
    }
}
