package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnTabItemClick")
/** Fires on `Tabs` when the tab whose id equals [itemId] is tapped.
 * @property itemId id of the tapped tab. */
data class OnTabItemClickEventTrigger(
    @SerialName("itemId")
    val itemId: String
) : EventTrigger