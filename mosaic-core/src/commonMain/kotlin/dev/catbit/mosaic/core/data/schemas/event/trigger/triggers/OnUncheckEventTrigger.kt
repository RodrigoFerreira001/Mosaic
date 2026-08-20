package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnUncheck")
/** Fires on `Checkbox`/`Switch`/`FilterChip`/`InputChip` when it becomes unchecked/unselected —
 * followed, on the same tap, by the generic `onCheckChanged()`. */
object OnUncheckEventTrigger : EventTrigger