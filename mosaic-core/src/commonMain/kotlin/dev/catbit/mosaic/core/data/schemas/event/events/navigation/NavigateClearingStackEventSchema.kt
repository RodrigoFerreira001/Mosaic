package dev.catbit.mosaic.core.data.schemas.event.events.navigation

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Navigates the graph registered under [navigatorId] to [destination], clearing the whole back
 * stack so the destination becomes the only entry — the usual move after login or logout.
 *
 * **Navigation data:** the destination receives the event's incomingData merged with [data], with
 * [data] winning on key collision. Only map-shaped incomingData contributes, and `null` values are
 * dropped from both — navigation arguments are never null.
 *
 * [launchSingleTop] (default `true`) avoids stacking a second copy when the destination is already
 * the current entry.
 *
 * **incomingData consumed:** merged into the destination's navigation data when it is a map.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the navigation was performed. No data is passed downstream.
 * - `OnFailureEventTrigger` — when no navigator is registered under [navigatorId], or the
 *   navigator refused the navigation; no data is passed and the error is logged.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("NavigateClearingStack")
data class NavigateClearingStackEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("destination") val destination: String,
    @SerialName("navigatorId") val navigatorId: String,
    @SerialName("launchSingleTop") val launchSingleTop: Boolean = true,
    @SerialName("data") val data: Map<String, AnySerializable>?
) : EventSchema
