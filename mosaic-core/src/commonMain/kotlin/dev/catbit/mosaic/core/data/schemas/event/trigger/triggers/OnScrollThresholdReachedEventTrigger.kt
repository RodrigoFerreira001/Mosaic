package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnScrollThresholdReached")
/** Fires on `LazyColumn`/`LazyRow` when the scroll position comes within `scrollThreshold` items of
 * the end of the list — the infinite-scroll pagination signal, guarded by
 * `Int.ThresholdReachedEffect` so it doesn't re-fire until the list actually grows. */
object OnScrollThresholdReachedEventTrigger : EventTrigger