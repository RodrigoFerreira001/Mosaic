package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.change_screen_state

import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema

object ChangeScreenStateEventHolderBuilder : EventHolderBuilder<ChangeScreenStateEventSchema, ChangeScreenStateEventHolder> {
    override fun BuilderScope.build(eventSchema: ChangeScreenStateEventSchema): ChangeScreenStateEventHolder =
        with(eventSchema) {
            ChangeScreenStateEventHolder(
                id = id,
                event = eventSchema,
                trigger = trigger,
                events = events?.map { eventModel -> buildEventHolder(eventModel) }
            )
        }
}
