package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnDisplay")
/** Fires once, the first time the declaring tile enters composition — implemented by every
 * built-in container/interactive tile via `TileRenderingScope.OnDisplayEffect()`
 * (`extensions/TileRenderingScopeExtensions.kt`), keyed on the tile's own id so it doesn't re-fire on
 * plain recomposition. */
object OnDisplayEventTrigger : EventTrigger