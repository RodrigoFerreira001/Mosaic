package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnLongPress")
/** Fires when an interactive tile is long-pressed — only made interactive for long-press when this
 * trigger is actually declared on the tile (e.g. `Box`/`Column`/`Row` only get a long-press handler
 * installed if something listens for it). */
object OnLongPressEventTrigger : EventTrigger