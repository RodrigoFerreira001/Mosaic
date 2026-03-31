package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.get_screen

import dev.catbit.mosaic.client.domain.screen.GetScreenUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object GetScreenEventRunner : EventRunner<GetScreenEventSchema> {
    override fun EventRunningScope.runEvent(event: GetScreenEventSchema) {
        @Suppress("UNCHECKED_CAST")
        runSuspendOnStateHolderScope {
            get<GetScreenUseCase>()(
                GetScreenUseCase.Params(
                    screenId = screenId,
                    headers = incomingData.asMapString(),
                )
            )
                .onSuccess { screenModel ->
                    onTrigger(
                        eventTrigger = EventTriggers.onSuccess(),
                        data = screenModel
                    )
                }
                .onFailure { failure ->
                    logError(failure)
                    onTrigger(
                        eventTrigger = EventTriggers.onFailure(),
                        data = failure
                    )
                }
        }
    }
}
