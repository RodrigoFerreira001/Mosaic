package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnSystemBroadcast")
data class OnSystemBroadcastEventTrigger(
    @SerialName("broadcastId") val broadcastId: String
) : EventTrigger