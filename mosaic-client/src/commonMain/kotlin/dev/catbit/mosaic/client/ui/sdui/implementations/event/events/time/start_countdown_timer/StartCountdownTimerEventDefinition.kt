package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_countdown_timer

import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartCountdownTimerEventSchema

object StartCountdownTimerEventDefinition : EventDefinition<StartCountdownTimerEventSchema> {
    override val eventSchemaClass = StartCountdownTimerEventSchema::class
    override val eventRunner = StartCountdownTimerEventRunner
    override val eventHolderBuilder = StartCountdownTimerEventHolderBuilder
}
