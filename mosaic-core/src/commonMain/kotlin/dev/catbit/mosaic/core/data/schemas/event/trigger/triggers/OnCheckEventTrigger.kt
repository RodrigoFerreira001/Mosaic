package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnCheck")
/** Fires on `Checkbox`/`Switch`/`FilterChip`/`InputChip` when it becomes checked/selected —
 * followed, on the same tap, by the generic `onCheckChanged()`. */
object OnCheckEventTrigger : EventTrigger