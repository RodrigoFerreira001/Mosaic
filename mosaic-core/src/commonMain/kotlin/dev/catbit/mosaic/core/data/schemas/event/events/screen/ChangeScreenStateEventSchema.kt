package dev.catbit.mosaic.core.data.schemas.event.events.screen

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ChangeScreenState")
data class ChangeScreenStateEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("state") val state: State
) : EventSchema {

    @Serializable
    sealed interface State {

        @SerialName("Success")
        data object Success : State

        @SerialName("Failure")
        data object Failure : State

        @SerialName("Initial")
        data object Initial : State
    }
}