package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnDataReceived")
/** Fires alongside `onSuccess()` on `CheckForReceivedData` when a value exists under the requested
 * `dataKey`, carrying that value as incoming data. */
object OnDataReceivedEventTrigger : EventTrigger