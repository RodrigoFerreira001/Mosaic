package dev.catbit.mosaic.core.data.schemas.event.events.navigation

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEventTrigger
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
data class NavigateEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    val destination: String,
    val navigatorId: String,
    val popUpTo: PopUpTo?,
    val data: Map<String, AnySerializable>?
) : EventSchema {

    @Serializable
    data class PopUpTo(
        val destination: String,
        val inclusive: Boolean
    )
}