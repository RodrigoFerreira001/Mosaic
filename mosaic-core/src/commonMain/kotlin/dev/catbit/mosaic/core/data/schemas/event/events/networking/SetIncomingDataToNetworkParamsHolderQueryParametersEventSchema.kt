package dev.catbit.mosaic.core.data.schemas.event.events.networking

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
 * Stores `incomingData` as the query parameters in the [NetworkParametersHolder], making them
 * available to the next network event ([SendNetworkRequestEventSchema], [GetScreenEventSchema],
 * [DownloadFileEventSchema]) that executes in the same event chain.
 *
 * The holder is consumed (and reset to `null`) on the next network call. `incomingData` must be
 * a `Map<String, Any?>` — each entry becomes a `?key=value` query parameter appended to the URL.
 * Null values within the map are skipped.
 *
 * **incomingData consumed:** Must be a non-null `Map<String, Any?>`. `null` incomingData or an
 * incompatible type fires [onFailure()] and nothing is stored.
 *
 * **Triggers fired:**
 * - [onSuccess()] – parameters stored successfully; incomingData is forwarded unchanged.
 * - [onFailure()] – incomingData is `null` or not a map; nothing is stored.
 *
 * **Typical usage:**
 * ```kotlin
 * GetData(trigger = EventTriggers.onClick(), readings = { ... }, events = {
 *     SetIncomingDataToNetworkParamsHolderQueryParameters(trigger = EventTriggers.onSuccess(), events = {
 *         SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/...", method = HttpMethod.GET)
 *         // SendNetworkRequest appends the query parameters from the holder to the URL
 *     })
 * })
 * ```
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("SetIncomingDataToNetworkParamsHolderQueryParameters")
data class SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema
