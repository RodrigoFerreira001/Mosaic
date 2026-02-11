package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.get_screen

import dev.catbit.mosaic.client.domain.screen.GetScreenUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object GetScreenEventRunner : EventRunner<GetScreenEventSchema> {
    override fun EventRunningScope.runEvent(event: GetScreenEventSchema) {
        getOrNull<GetScreenUseCase>()?.let { getScreenUseCase ->

            @Suppress("UNCHECKED_CAST")
            runSuspendOnStateHolderScope {
                getScreenUseCase(
                    GetScreenUseCase.Params(
                        screenId = event.screenId,
                        headers = incomingData?.asMapAny()?.filterValues { it is String } as? Map<String, String>
                    )
                )
                    .onSuccess { screenModel ->
                        onTrigger(
                            eventTrigger = EventTriggers.onSuccess(),
                            data = screenModel
                        )
                    }
                    .onFailure { failure ->
                        onTrigger(
                            eventTrigger = EventTriggers.onFailure(),
                            data = failure
                        )
                    }
            }
        }
    }
}
