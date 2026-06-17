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
 * Stores `incomingData` as the request headers in the [NetworkParametersHolder], making them
 * available to the next network event ([SendNetworkRequestEventSchema], [GetScreenEventSchema],
 * [DownloadFileEventSchema]) that executes in the same event chain.
 *
 * The holder is consumed (and reset to `null`) on the next network call. Headers from the holder
 * are **merged with** explicit `headers` in the network schema — the schema's headers take
 * precedence on key collision: `finalHeaders = holder.headers + schema.headers`.
 *
 * **incomingData consumed:** Must be castable to `Map<String, String>`. If cast fails (e.g.
 * incomingData is `null` or not a string-keyed map), [onFailure()] is fired and nothing is stored.
 *
 * **Triggers fired:**
 * - [onSuccess()] – headers stored successfully; incomingData is forwarded unchanged.
 * - [onFailure()] – incomingData cannot be cast to `Map<String, String>`.
 *
 * **Typical usage:**
 * ```kotlin
 * GetData(trigger = EventTriggers.onClick(), readings = {
 *     reading(segmentedDataBase("auth"), singleAccessMode("token"))
 * }, events = {
 *     // incomingData = {"token": "Bearer xyz"} after GetData
 *     SetIncomingDataToNetworkParamsHolderHeaders(trigger = EventTriggers.onSuccess(), events = {
 *         SendNetworkRequest(trigger = EventTriggers.onSuccess(), url = "/api/protected", method = HttpMethod.GET)
 *         // request goes out with Authorization header from holder
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
@SerialName("SetIncomingDataToNetworkParamsHolderHeaders")
data class SetIncomingDataToNetworkParamsHolderHeadersEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema