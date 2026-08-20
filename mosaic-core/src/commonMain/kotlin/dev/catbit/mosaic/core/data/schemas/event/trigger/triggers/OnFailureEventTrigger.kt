package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnFailure")
/** The generic failure outcome, fired by nearly every event in the catalog when its work doesn't
 * succeed. Often carries the causing `Throwable`/parsed error body as incoming data — see the
 * matching event's own catalog entry for exactly what. */
object OnFailureEventTrigger : EventTrigger