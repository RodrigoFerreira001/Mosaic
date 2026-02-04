package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData
import dev.catbit.mosaic.core.data.models.screen.ScreenModel

interface ScreenBehaviorsHolder {
    fun broadcastData(data: BroadcastData)
    fun setState(state: State)

    sealed interface State {
        data class Success(
            val screenModel: ScreenModel
        ) : State

        data object Failure : State
        data object Initial : State
    }
}