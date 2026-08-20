package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnNavigationEntrySet")
/** Fires on `NestedNavigationGraph` whenever an entry is displayed — including the start
 * destination, and again every time navigation returns to that entry — carrying the entry's own
 * `screenId`.
 * @property screenId id of the entry that was just displayed. */
data class OnNavigationEntrySetEventTrigger(
    @SerialName("screenId") val screenId: String
) : EventTrigger