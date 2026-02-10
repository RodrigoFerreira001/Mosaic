package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.change_screen_state

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema

object ChangeScreenStateEventRunner : EventRunner<ChangeScreenStateEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: ChangeScreenStateEventSchema) {
        println("Running ChangeScreenStateEvent: $event")
    }
}
