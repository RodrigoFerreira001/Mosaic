package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnDateSelected")
/** Fires on `DatePicker` when the confirm button is pressed, carrying the picked ISO date string
 * (`yyyy-MM-dd`) as incoming data. */
data object OnDateSelectedEventTrigger : EventTrigger
