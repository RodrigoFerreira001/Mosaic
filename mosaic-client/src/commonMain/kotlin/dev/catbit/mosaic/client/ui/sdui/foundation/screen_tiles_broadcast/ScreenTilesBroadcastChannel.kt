package dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Immutable
class ScreenTilesBroadcastChannel {

    private val internalChannel = MutableSharedFlow<ScreenTilesBroadcastData>()
    val channel get() = internalChannel.asSharedFlow()

    suspend fun broadcast(
        data: ScreenTilesBroadcastData
    ) {
        internalChannel.emit(
            data
        )
    }
}