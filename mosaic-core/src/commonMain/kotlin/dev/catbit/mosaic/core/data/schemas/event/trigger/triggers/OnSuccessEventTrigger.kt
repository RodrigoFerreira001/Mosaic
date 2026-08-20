package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnSuccess")
/** The generic success outcome, fired by nearly every event in the catalog when its work succeeds.
 * Often carries the produced value as incoming data — see the matching event's own catalog entry
 * for exactly what. */
object OnSuccessEventTrigger : EventTrigger