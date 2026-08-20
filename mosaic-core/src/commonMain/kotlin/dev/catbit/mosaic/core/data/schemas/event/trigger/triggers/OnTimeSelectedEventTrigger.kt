package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnTimeSelected")
/** Fires on `TimePicker` when the confirm button is pressed, carrying the picked ISO time string
 * (`HH:mm`) as incoming data. */
data object OnTimeSelectedEventTrigger : EventTrigger
