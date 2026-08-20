package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnTrailingIconClick")
/** Fires on `TextField` when `trailingIcon` is tapped — only when `clickableTrailingIcon` is
 * `true` (defaults to `true`, matching the common "clear button" pattern). */
object OnTrailingIconClickEventTrigger : EventTrigger