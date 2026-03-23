package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OnNavigationBarItemClick")
data class OnNavigationBarItemClickEventTrigger(
    @SerialName("itemId")
    val itemId: String
) : EventTrigger