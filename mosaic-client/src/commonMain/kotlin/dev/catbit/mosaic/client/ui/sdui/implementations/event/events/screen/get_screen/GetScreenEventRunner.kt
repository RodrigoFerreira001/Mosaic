package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.get_screen

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema

object GetScreenEventRunner : EventRunner<GetScreenEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: GetScreenEventSchema) {
        println("Running GetScreenEvent: $event")
    }
}
