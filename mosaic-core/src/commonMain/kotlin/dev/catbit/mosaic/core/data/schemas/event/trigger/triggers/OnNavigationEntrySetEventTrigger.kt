package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnNavigationEntrySet")
data class OnNavigationEntrySetEventTrigger(
    @SerialName("screenId") val screenId: String
) : EventTrigger