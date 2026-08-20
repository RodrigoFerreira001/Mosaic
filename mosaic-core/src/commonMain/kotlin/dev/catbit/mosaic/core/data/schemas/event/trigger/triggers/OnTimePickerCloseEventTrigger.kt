package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnTimePickerClose")
/** Fires on `TimePicker` whenever the dialog closes — after confirming, on cancel, or on
 * dismiss. */
data object OnTimePickerCloseEventTrigger : EventTrigger
