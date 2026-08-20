package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnNavigationBarItemClick")
/** Fires on `NavigationBar` when the item whose id equals [itemId] is tapped.
 * @property itemId id of the tapped item. */
data class OnNavigationBarItemClickEventTrigger(
    @SerialName("itemId")
    val itemId: String
) : EventTrigger