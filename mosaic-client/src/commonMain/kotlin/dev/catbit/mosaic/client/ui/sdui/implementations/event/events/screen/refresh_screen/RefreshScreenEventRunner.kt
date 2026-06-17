package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.refresh_screen

import dev.catbit.mosaic.client.domain.screen.GetScreenUseCase
import dev.catbit.mosaic.client.exceptions.NetworkResponseException
import dev.catbit.mosaic.client.extensions.toKtorHttpMethod
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviorsHolder
import dev.catbit.mosaic.core.data.schemas.event.events.screen.RefreshScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkFailureEventTrigger

object RefreshScreenEventRunner : EventRunner<RefreshScreenEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: RefreshScreenEventSchema) {

        screenBehaviorsHolder.setState(ScreenBehaviorsHolder.State.Initial)

        @Suppress("UNCHECKED_CAST")
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
                screenBehaviorsHolder.setState(ScreenBehaviorsHolder.State.Failure)

                val hasCustomNetworkFailureListener = failure is NetworkResponseException &&
                        event.events?.any {
                            it.trigger == OnNetworkFailureEventTrigger(failure.status.value)
                        } == true

                if (hasCustomNetworkFailureListener) {
                    onTrigger(
                        eventTrigger = EventTriggers.onNetworkFailure(failure.status.value),
                        data = failure
                    )
                } else {
                    onTrigger(
                        eventTrigger = EventTriggers.onFailure(),
                        data = failure
                    )
                }
                logError(
                    tag = "RefreshScreenEventRunner",
                    throwable = failure
                )
            }
    }
}
