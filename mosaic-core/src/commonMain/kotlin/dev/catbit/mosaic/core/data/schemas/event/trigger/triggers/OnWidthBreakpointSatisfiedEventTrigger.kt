package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnWidthBreakpointSatisfied")
/** Fires on `AdaptiveVisibility` on first composition and every width change, when its
 * `widthVisibility` condition is satisfied. */
object OnWidthBreakpointSatisfiedEventTrigger : EventTrigger
