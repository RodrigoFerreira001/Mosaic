package dev.catbit.mosaic.core.data.schemas.event.events.screen

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Fetches a screen definition from the server using the current screen's ID and propagates
 * the result through child events. The screen ID is not encoded in this schema; it is resolved
 * at runtime from `EventRunningScope.screenId` (the ID of the screen that owns this event).
 *
 * **incomingData consumed:** Not consumed directly. To pass data as request body or headers,
 * use [SetIncomingDataToNetworkParamsHolderBodyEventSchema] or
 * [SetIncomingDataToNetworkParamsHolderHeadersEventSchema] before this event.
 *
 * **Request body/headers resolution:** Same mechanism as [SendNetworkRequestEventSchema]:
 * - Body: schema [body] ?? holder.body
 * - Headers: holder.headers + schema.headers (schema takes precedence on collision)
 * The holder is always consumed on execution.
 *
 * **Triggers fired:**
 * - [onStart()] – fired immediately before the network request is issued.
 * - [onSuccess()] – server returned a valid `ScreenModel`; incomingData becomes the `ScreenModel`.
 * - [onFailure()] – fired on failure when no child event declares a matching [onNetworkFailure(httpCode)]
 *   trigger; incomingData becomes the `Throwable`. Logged via `logError`. Always fired for
 *   network/IO exceptions or deserialization errors regardless of child triggers.
 * - [onNetworkFailure(httpCode)] – fired **instead of** [onFailure()] when the server responded
 *   with a non-2xx HTTP status AND a child event declares a matching `onNetworkFailure(httpCode)`
 *   trigger for that status code; incomingData becomes the `NetworkResponseException`.
 *
 * **Failure scenarios:**
 * - Non-2xx HTTP response with matching child trigger: fires ONLY [onNetworkFailure(httpCode)].
 * - Non-2xx HTTP response without matching child trigger: fires ONLY [onFailure()].
 * - Network/IO exception or deserialization error: fires ONLY [onFailure()].
 *
 * **Notes:**
 * - Unlike [RefreshScreenEventSchema], this event does NOT automatically apply the fetched
 *   `ScreenModel` to the screen state. Chain a [ChangeScreenStateEventSchema] on [onSuccess()]
 *   to apply the result.
 */
@Immutable
@Triggers(
    [
        OnStartEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
        OnNetworkFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("GetScreen")
data class GetScreenEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("method") val method: HttpMethod = HttpMethod.GET,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?,
) : EventSchema