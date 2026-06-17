package dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationDrawerDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Closes the screen's navigation drawer by broadcasting a dismiss signal to the active screen.
 * No tile data is altered.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnNavigationDrawerDismissedEventTrigger] — fired by the screen's overlay container after
 *   the drawer has been fully closed (the broadcast is handled by
 *   [ScreenTileBroadcastData.DismissNavigationDrawer]).
 *
 * **Failure scenarios:** None defined. The runner unconditionally broadcasts the dismiss
 * signal regardless of whether the drawer is currently open.
 *
 * **Notes:** If the drawer is already closed, the broadcast is a no-op on the UI side.
 * The [OnNavigationDrawerDismissedEventTrigger] is typically fired by the overlay container,
 * not directly by this runner.
 */
@Immutable
@Triggers(
    [
        OnNavigationDrawerDismissedEventTrigger::class,
        OnSuccessEventTrigger::class
    ]
)
@Serializable
@SerialName("DismissNavigationDrawer")
data class DismissNavigationDrawerEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema