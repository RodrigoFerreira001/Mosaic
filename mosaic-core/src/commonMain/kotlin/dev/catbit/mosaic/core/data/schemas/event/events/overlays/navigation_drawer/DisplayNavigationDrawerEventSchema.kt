package dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Opens the screen's navigation drawer by broadcasting an open signal to the active screen.
 * The drawer's content is defined by the screen tile itself, not by this event — no tile
 * data is supplied here.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:** None. This event fires no lifecycle triggers; it only pushes the
 * overlay state to the screen via [ScreenTileBroadcastData.DisplayNavigationDrawer].
 *
 * **Failure scenarios:** None defined. The runner does not perform any fallible operation.
 *
 * **Notes:** The navigation drawer must already be configured on the screen tile for this
 * event to have a visible effect. Sending this event when the drawer is already open is
 * a no-op on the UI side.
 */
@Triggers(
    [
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("DisplayNavigationDrawer")
data class DisplayNavigationDrawerEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?
) : EventSchema