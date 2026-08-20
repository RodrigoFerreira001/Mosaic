package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnSelected")
/** Fires on `RadioButton` only for the radio button that becomes selected — the ones that lose
 * selection within the same `groupId` fire nothing. */
object OnSelectEventTrigger : EventTrigger