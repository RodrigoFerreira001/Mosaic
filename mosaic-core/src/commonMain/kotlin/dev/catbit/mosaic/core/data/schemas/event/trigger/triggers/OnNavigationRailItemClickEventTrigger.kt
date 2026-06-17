package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnNavigationRailItemClick")
data class OnNavigationRailItemClickEventTrigger(
    @SerialName("itemId")
    val itemId: String
) : EventTrigger