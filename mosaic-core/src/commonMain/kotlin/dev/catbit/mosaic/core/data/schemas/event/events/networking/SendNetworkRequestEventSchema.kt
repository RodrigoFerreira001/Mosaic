package dev.catbit.mosaic.core.data.schemas.event.events.networking

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkResponseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sends an HTTP request to [url] with [method], [headers], [body] and an optional
 * [timeoutMillis], and emits the response downstream. This is the general-purpose way to talk to a
 * backend from inside an event chain.
 *
 * **Response shape:** a JSON content type is parsed into plain maps, lists and primitives;
 * anything else is passed through as a raw `ByteArray`. A JSON body that fails to parse yields
 * `null`.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnStartEventTrigger` — before the request is sent.
 * - `OnNetworkResponseEventTrigger` / `OnNetworkFailureEventTrigger` — when this event declares an
 *   event wired to the response's exact HTTP status: the first for 2xx statuses, the second for
 *   the rest. The parsed body is passed as incomingData.
 * - `OnSuccessEventTrigger` — when the response is 2xx and no status-specific event was declared;
 *   the parsed body is passed as incomingData.
 * - `OnFailureEventTrigger` — when the response is not 2xx and no status-specific event was
 *   declared (the parsed body is passed), and whenever the request itself fails — timeout, no
 *   connectivity, and so on — in which case the `Throwable` is passed and the error is logged.
 *
 * Exactly one of these outcome triggers fires per response.
 */
@Immutable
@Triggers(
    [
        OnStartEventTrigger::class,
        OnNetworkResponseEventTrigger::class,
        OnNetworkFailureEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("SendNetworkRequest")
data class SendNetworkRequestEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("url") val url: String,
    @SerialName("method") val method: HttpMethod,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?,
    @SerialName("timeoutMillis") val timeoutMillis: Long? = null
) : EventSchema