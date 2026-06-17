package dev.catbit.mosaic.core.data.schemas.event.events.networking

import androidx.compose.runtime.Immutable
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
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

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
 * - [onSuccess()] – fired on a 2xx response when **no** child event declares a matching
 *   [onNetworkResponse(statusCode)] or [onNetworkFailure(statusCode)] trigger for that status;
 *   incomingData becomes the parsed response body (`JsonElement → Any` for JSON, `ByteArray`
 *   for all other content types).
 * - [onFailure()] – fired on a non-2xx response when **no** matching custom listener exists,
 *   or on any network/IO exception; incomingData is the parsed response body for HTTP errors
 *   or the `Throwable` for network exceptions.
 * - [onNetworkResponse(statusCode)] – fired **instead of** [onSuccess()] when a child event
 *   declares a matching trigger for a **2xx** status code; incomingData is the parsed response
 *   body.
 * - [onNetworkFailure(statusCode)] – fired **instead of** [onFailure()] when a child event
 *   declares a matching trigger for a **non-2xx** status code; incomingData is the parsed
 *   response body. Never fired on network/IO exceptions.
 *
 * A child event activates custom dispatch for a given status code if it declares either
 * [onNetworkResponse(statusCode)] **or** [onNetworkFailure(statusCode)] for that code.
 *
 * **Failure scenarios:**
 * - Non-2xx with matching listener: fires ONLY [onNetworkFailure(statusCode)].
 * - Non-2xx without matching listener: fires ONLY [onFailure()].
 * - Network/IO exception: fires ONLY [onFailure()] with the `Throwable`.
 */
@Immutable
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
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("url") val url: String,
    @SerialName("method") val method: HttpMethod,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?
) : EventSchema