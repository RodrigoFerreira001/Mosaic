package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnTimePickerOpen")
/** Fires on `TimePicker` when the field is pressed while the dialog is closed. */
data object OnTimePickerOpenEventTrigger : EventTrigger
