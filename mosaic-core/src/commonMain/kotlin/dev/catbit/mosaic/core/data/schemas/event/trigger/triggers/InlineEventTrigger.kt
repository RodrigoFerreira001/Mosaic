package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("Inline")
/** The trigger a child event uses when it's meant to run unconditionally as part of a plain fan-out
 * (`RunEvents`, `RunCancellableEvents`) rather than being matched against a real outcome — those two
 * events run their own `events` list unconditionally regardless of what `trigger` each child
 * declares, so `inline()` documents that intent at the call site instead of implying a real
 * condition like `onSuccess()` would. */
object InlineEventTrigger : EventTrigger