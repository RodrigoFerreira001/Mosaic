package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.get_screen

import dev.catbit.mosaic.client.domain.screen.GetScreenUseCase
import dev.catbit.mosaic.client.extensions.toKtorHttpMethod
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object GetScreenEventRunner : EventRunner<GetScreenEventSchema> {
    override fun EventRunningScope.runEvent(event: GetScreenEventSchema) {

        @Suppress("UNCHECKED_CAST")
        runSuspendOnStateHolderScope {
            get<GetScreenUseCase>()(
                with(event) {
                    GetScreenUseCase.Params(
                        screenId = screenId,
                        headers = headers,
                        body = body,
                        httpMethod = method.toKtorHttpMethod()
                    )
                }
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
                    logError(
                        tag = "GetScreenEventRunner",
                        throwable = failure
                    )
                }
        }
    }
}
