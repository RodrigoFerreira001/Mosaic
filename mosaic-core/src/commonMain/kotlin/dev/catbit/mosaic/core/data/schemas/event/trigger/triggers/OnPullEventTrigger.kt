package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnPull")
/** Fires on `PullToRefresh` when the user completes a pull gesture — the trigger to hook the real
 * refresh work to. */
object OnPullEventTrigger : EventTrigger