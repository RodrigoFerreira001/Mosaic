package dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationDrawerDismissedEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Triggers(
    [
        OnNavigationDrawerDismissedEventTrigger::class
    ]
)
@Serializable
@SerialName("DismissNavigationDrawer")
data class DismissNavigationDrawerEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?
) : EventSchema