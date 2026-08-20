package dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast

import dev.catbit.mosaic.core.serialization.serializers.AnySerializable

/**
 * One value published on [SystemBroadcastChannel] — carries its own [broadcastId] alongside [data]
 * so a single shared flow can multiplex every channel id, and a `SystemBroadcastListener` (or
 * `observeSystemBroadcastChannel`) filters by comparing [broadcastId] against the one it's listening
 * for.
 *
 * @property broadcastId channel id this value was published under.
 * @property data the published payload.
 */
data class SystemBroadcastData(
    val broadcastId: String,
    val data: AnySerializable,
)