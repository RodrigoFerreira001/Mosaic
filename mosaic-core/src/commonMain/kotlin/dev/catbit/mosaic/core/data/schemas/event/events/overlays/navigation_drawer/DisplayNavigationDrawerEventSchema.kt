package dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Opens the screen's navigation drawer, by broadcasting an open command on the screen channel.
 * The drawer content is declared on the screen itself, so this event carries no parameters and
 * there is only ever one drawer per screen.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — always, right after the command was broadcast. The broadcast is
 *   fire-and-forget, so this fires even when the screen declares no drawer. No data is passed
 *   downstream.
 */
@Immutable
@Triggers([OnSuccessEventTrigger::class])
@Serializable
@SerialName("DisplayNavigationDrawer")
data class DisplayNavigationDrawerEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema