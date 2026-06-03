package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.refresh_screen

import dev.catbit.mosaic.client.domain.screen.GetScreenUseCase
import dev.catbit.mosaic.client.extensions.toKtorHttpMethod
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviorsHolder
import dev.catbit.mosaic.core.data.schemas.event.events.screen.RefreshScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers

object RefreshScreenEventRunner : EventRunner<RefreshScreenEventSchema> {
    override fun EventRunningScope.runEvent(event: RefreshScreenEventSchema) {

        screenBehaviorsHolder.setState(ScreenBehaviorsHolder.State.Initial)

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
                    screenBehaviorsHolder.setState(ScreenBehaviorsHolder.State.Success(screenModel))
                    onTrigger(
                        eventTrigger = EventTriggers.onSuccess(),
                        data = screenModel
                    )
                }
                .onFailure { failure ->
                    logError(
                        tag = "RefreshScreenEventRunner",
                        throwable = failure
                    )
                    screenBehaviorsHolder.setState(ScreenBehaviorsHolder.State.Failure)
                    onTrigger(
                        eventTrigger = EventTriggers.onFailure(),
                        data = failure
                    )
                }
        }
    }
}
