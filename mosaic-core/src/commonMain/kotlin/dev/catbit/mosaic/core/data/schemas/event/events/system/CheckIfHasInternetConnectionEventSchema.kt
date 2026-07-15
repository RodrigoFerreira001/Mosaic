package dev.catbit.mosaic.core.data.schemas.event.events.system

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Checks whether the device currently has an active internet connection by issuing a lightweight
 * HTTP GET request to a captive-portal-style probe endpoint.
 *
 * **incomingData consumed:** Not used.
 *
 * **Triggers fired:**
 * - [OnSuccessEventTrigger] — fires when the probe request succeeds, with no incomingData payload.
 * - [OnFailureEventTrigger] — fires when the probe request fails (no connection, timeout, or
 *   non-success response), with no incomingData payload.
 *
 * **Failure scenarios:** Any exception raised by the underlying HTTP client (no connection, DNS
 * failure, timeout) is caught and treated as "no internet connection" rather than propagated.
 *
 * **Notes:** This event carries no extra fields beyond the base [EventSchema] contract. The
 * check is a reachability probe, not a guarantee that any particular host is reachable.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("CheckIfHasInternetConnection")
data class CheckIfHasInternetConnectionEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema