package dev.catbit.mosaic.core.data.schemas.event.events.networking

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stores `incomingData` as the request body in the [NetworkParametersHolder], making it available
 * to the next network event ([SendNetworkRequestEventSchema], [GetScreenEventSchema],
 * [DownloadFileEventSchema]) that executes in the same event chain.
 *
 * The holder is consumed (and reset to `null`) on the next network call, regardless of whether
 * this event set anything. Body and headers set via the holder are overridden by explicit
 * `body`/`headers` values in the network schema if both are present — the schema takes precedence.
 *
 * **incomingData consumed:** Any non-null value is stored as the body. `null` incomingData fires
 * [onFailure()] and nothing is stored.
 *
 * **Triggers fired:**
 * - [onSuccess()] – body stored successfully; incomingData is forwarded unchanged.
 * - [onFailure()] – incomingData is `null`; nothing is stored.
 *
 * **Typical usage:**
 * ```kotlin
 * GetData(trigger = EventTriggers.onClick(), readings = { ... }, events = {
 *     SetIncomingDataToNetworkParamsHolderBody(trigger = EventTriggers.onSuccess(), events = {
 *         SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/...", method = HttpMethod.POST)
 *         // SendNetworkRequest picks up the body from the holder automatically
 *     })
 * })
 * ```
 */
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("SetIncomingDataToNetworkParamsHolderBody")
data class SetIncomingDataToNetworkParamsHolderBodyEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?
) : EventSchema