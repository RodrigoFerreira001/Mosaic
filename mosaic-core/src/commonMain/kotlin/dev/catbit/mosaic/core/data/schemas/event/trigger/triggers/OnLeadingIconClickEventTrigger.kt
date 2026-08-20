package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnLeadingIconClick")
/** Fires on `TextField` when `leadingIcon` is tapped — only when `clickableLeadingIcon` is
 * `true` (defaults to `false`, unlike `trailingIcon`'s default). */
object OnLeadingIconClickEventTrigger : EventTrigger