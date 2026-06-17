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
 * Stores `incomingData` as the request URL in the [NetworkParametersHolder], making it available
 * to the next network event in the same chain. Today it is consumed by
 * [SendFileEventSchema] (when its `url` is `null`) — the typical use is feeding a **signed URL**
 * obtained at runtime from the backend into the upload request.
 *
 * The holder is consumed (and reset to `null`) on the next network call, regardless of whether
 * this event set anything. A non-null `url` in the network schema takes precedence over the
 * holder value.
 *
 * **incomingData consumed:** A `String` is stored as the URL. `null` or non-`String`
 * incomingData fires [onFailure()] and nothing is stored.
 *
 * **Triggers fired:**
 * - [onSuccess()] – URL stored successfully; incomingData is forwarded unchanged.
 * - [onFailure()] – incomingData is `null` or not a `String`; nothing is stored.
 *
 * **Typical usage:**
 * ```kotlin
 * SendNetworkRequest(trigger = EventTriggers.onClick(), url = "/uploads", method = HttpMethod.POST, events = {
 *     TransformData(trigger = EventTriggers.onSuccess(), /* extrai "uploadUrl" */ events = {
 *         SetIncomingDataToNetworkParamsHolderUrl(trigger = EventTriggers.onSuccess(), events = {
 *             // ... evento que produz os bytes, e então:
 *             SendFile(trigger = EventTriggers.onSuccess(), contentType = "video/mp4")
 *         })
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
@SerialName("SetIncomingDataToNetworkParamsHolderUrl")
data class SetIncomingDataToNetworkParamsHolderUrlEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?
) : EventSchema
