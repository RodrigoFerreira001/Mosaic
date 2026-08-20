package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnSystemBroadcast")
/** Fires on `SystemBroadcastListener` when a value is published on the app-wide system broadcast
 * channel under [broadcastId] (via `BroadcastToSystem`), carrying the published value as incoming
 * data.
 * @property broadcastId channel id this trigger listens on. */
data class OnSystemBroadcastEventTrigger(
    @SerialName("broadcastId") val broadcastId: String
) : EventTrigger