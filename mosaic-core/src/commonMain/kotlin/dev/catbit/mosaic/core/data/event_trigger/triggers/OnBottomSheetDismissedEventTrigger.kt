package dev.catbit.mosaic.core.data.event_trigger.triggers

import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OnBottomSheetDismissed")
object OnBottomSheetDismissedEventTrigger : EventTrigger