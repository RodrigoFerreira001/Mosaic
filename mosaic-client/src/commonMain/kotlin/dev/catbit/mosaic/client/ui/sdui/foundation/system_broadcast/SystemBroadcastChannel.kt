package dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast

import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SystemBroadcastChannel {
    private val internalChannel = MutableSharedFlow<SystemBroadcastData>()
    val channel get() = internalChannel.asSharedFlow()

    suspend fun broadcast(
        broadcastId: String,
        data: AnySerializable
    ) {
        internalChannel.emit(
            SystemBroadcastData(
                broadcastId = broadcastId,
                data = data
            )
        )
    }
}