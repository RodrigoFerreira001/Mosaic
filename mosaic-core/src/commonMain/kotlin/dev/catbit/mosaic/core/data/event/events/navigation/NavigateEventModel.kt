package dev.catbit.mosaic.core.data.event.events.navigation

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.event_trigger.triggers.OnNavigationEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnNavigationEventTrigger::class
    ]
)
@Serializable
@SerialName("Navigate")
data class NavigateEventModel(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventModel>?,
    val destination: String,
    val navigatorId: String,
    val popUpTo: PopUpTo?,
    val data: Map<String, AnySerializable>?
) : EventModel {

    @Serializable
    data class PopUpTo(
        val destination: String,
        val inclusive: Boolean
    )
}