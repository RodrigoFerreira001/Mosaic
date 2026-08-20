package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnLoadTilesSuccess")
/** Fires on `LazyTiles` when its network fetch succeeds and the response was decoded into a tile
 * list. */
object OnLoadTilesSuccessEventTrigger : EventTrigger