package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnPause")
/** Fires on the screen itself (a screen-level trigger, not tied to any one tile) when the screen's
 * lifecycle pauses — the client's internal `ScreenTileRenderer` wires this to Compose's
 * `LifecycleResumeEffect`/`onPauseOrDispose`. */
object OnPauseEventTrigger : EventTrigger
