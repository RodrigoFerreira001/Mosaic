package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnCountdownTimerTick")
/** Fires repeatedly on `StartCountdownTimer`, once per tick, carrying the remaining value as
 * incoming data. */
data object OnCountdownTimerTickEventTrigger : EventTrigger