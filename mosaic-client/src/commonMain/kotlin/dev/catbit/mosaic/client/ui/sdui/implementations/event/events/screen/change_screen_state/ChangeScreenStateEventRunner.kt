package dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.change_screen_state

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenBehaviorsHolder
import dev.catbit.mosaic.core.data.models.screen.ScreenModel
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.extensions.runSafely

object ChangeScreenStateEventRunner : EventRunner<ChangeScreenStateEventSchema> {
    override fun EventRunningScope.runEvent(event: ChangeScreenStateEventSchema) {
        runSafely(
            onError = {
                onTrigger(EventTriggers.onFailure())
                logError(
                    tag = "ChangeScreenStateEventRunner",
                    throwable = it
                )
            }
        ) {
            screenBehaviorsHolder.setState(
                when (val eventState = event.state) {
                    ChangeScreenStateEventSchema.State.Failure -> ScreenBehaviorsHolder.State.Failure
                    ChangeScreenStateEventSchema.State.Initial -> ScreenBehaviorsHolder.State.Initial
                    is ChangeScreenStateEventSchema.State.Success -> ScreenBehaviorsHolder.State.Success(
                        screenModel = eventState.data?.let { data ->
                            ScreenModel(
                                tiles = data.tiles,
                                navigationDrawerTiles = data.navigationDrawerTiles,
                                events = data.events,
                            )
                        }
                            ?: incomingData as? ScreenModel
                            ?: throw IllegalArgumentException("ChangeScreenState needs a valid ScreenModel and State is Success")
                    )
                }
            )
            onTrigger(EventTriggers.onSuccess())
        }
    }
}
