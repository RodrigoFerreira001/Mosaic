package dev.catbit.mosaic.core.data.schemas.event.events.networking

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkResponseTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Performs an HTTP request to [url] using [method] and propagates the result through child events.
 *
 * **incomingData consumed:** Not consumed directly. To pass data from a previous event as the
 * request body or headers, use [SetIncomingDataToNetworkParamsHolderBodyEventSchema] or
 * [SetIncomingDataToNetworkParamsHolderHeadersEventSchema] before this event in the chain.
 *
 * **Request body resolution (in priority order):**
 * 1. [body] (explicit schema value) — used if non-null.
 * 2. `NetworkParametersHolder.body` — used if schema [body] is null and the holder was populated
 *    by a preceding [SetIncomingDataToNetworkParamsHolderBodyEventSchema] in the same chain.
 * 3. `null` — no body sent.
 *
 * **Request headers resolution:**
 * `finalHeaders = holder.headers + schema.headers` (schema takes precedence on key collision).
 * The holder is always consumed (reset to null) on execution.
 *
 * **Triggers fired:**
 * - [onStart()] – immediately before the HTTP request is dispatched.
 * - [onSuccess()] – 2xx response; incomingData becomes the parsed response body
 *   (`JsonElement → Any` for JSON, `ByteArray` for all other content types).
 * - [onFailure()] – non-2xx status or network/IO exception; incomingData is the response body
 *   for HTTP errors, or the `Throwable` for network exceptions.
 * - [onNetworkResponse(statusCode)] – fired after any completed HTTP response (including non-2xx);
 *   incomingData is the same parsed response body. NOT fired on network exceptions.
 *
 * **Failure scenarios:**
 * - Non-2xx HTTP status: fires BOTH [onFailure()] AND [onNetworkResponse()] with the same body.
 * - Network/IO exception: fires ONLY [onFailure()] with the `Throwable`.
 *
 * **Notes:**
 * - [onNetworkResponse()] co-fires with [onSuccess()] for 2xx responses — child events on both
 *   triggers will execute simultaneously for the same response.
 */
@Triggers(
    [
        OnStartEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
        OnNetworkResponseTrigger::class
    ]
)
@Serializable
@SerialName("SendNetworkRequest")
data class SendNetworkRequestEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("url") val url: String,
    @SerialName("method") val method: HttpMethod,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?
) : EventSchema