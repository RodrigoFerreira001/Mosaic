package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnKeyboardSend")
/** Fires on `TextField` when the IME action button is pressed and `keyboardOptions.imeAction` is
 * `Send`. */
data object OnKeyboardSendEventTrigger : EventTrigger