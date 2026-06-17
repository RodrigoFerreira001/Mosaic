package dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast

import dev.catbit.mosaic.core.serialization.serializers.AnySerializable

data class SystemBroadcastData(
    val broadcastId: String,
    val data: AnySerializable,
)