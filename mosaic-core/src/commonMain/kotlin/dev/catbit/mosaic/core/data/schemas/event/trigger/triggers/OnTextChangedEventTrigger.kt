package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnTextChanged")
/** Fires on `TextField` on every keystroke where the text actually changes vs. its current `value`,
 * carrying the new text as incoming data. */
object OnTextChangedEventTrigger : EventTrigger