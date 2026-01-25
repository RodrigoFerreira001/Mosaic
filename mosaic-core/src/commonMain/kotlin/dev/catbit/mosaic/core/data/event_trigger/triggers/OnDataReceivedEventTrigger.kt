package dev.catbit.mosaic.core.data.event_trigger.triggers

import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OnDataReceived")
object OnDataReceivedEventTrigger : EventTrigger