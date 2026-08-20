package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnQueryChanged")
/** Fires on `SearchBar` on every keystroke where the query text actually changes, carrying the new
 * query as incoming data. */
object OnQueryChangedEventTrigger : EventTrigger