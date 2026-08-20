package dev.catbit.mosaic.core.data.schemas.event.trigger.triggers

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@SerialName("OnHeightBreakpointSatisfied")
/** Fires on `AdaptiveVisibility` on first composition and every height change, when its
 * `heightVisibility` condition is satisfied. */
object OnHeightBreakpointSatisfiedEventTrigger : EventTrigger
