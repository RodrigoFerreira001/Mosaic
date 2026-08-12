package dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
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
 * - [OnSuccessEventTrigger] — fired as soon as the dismiss signal is broadcast, **not** after the
 *   closing animation ends.
 *
 * **Failure scenarios:** None defined. The runner unconditionally broadcasts the dismiss signal
 * regardless of whether the drawer is currently open.
 *
 * **Notes:** If the drawer is already closed — or if the screen defines no `navigationDrawerTiles`
 * at all — the broadcast is a no-op on the UI side and still reports success.
 */
@Immutable
@Triggers(
    [
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