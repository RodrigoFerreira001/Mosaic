package dev.catbit.mosaic.core.data.schemas.event.events.screen

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Reloads the current screen by resetting it to `Initial` state, fetching its definition from
 * the server, and applying the result to the screen state automatically.
 * Unlike [GetScreenEventSchema], this event is fully self-contained and manages screen state.
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
 * - [onSuccess()] – server returned a valid `ScreenModel` and it has been applied to the screen
 *   state (`Success`); incomingData becomes the `ScreenModel`.
 * - [onFailure()] – fired on failure when no child event declares a matching [onNetworkFailure(httpCode)]
 *   trigger; screen state is set to `Failure`; incomingData becomes the `Throwable`. Logged via `logError`.
 *   Always fired for network/IO exceptions or deserialization errors regardless of child triggers.
 * - [onNetworkFailure(httpCode)] – fired **instead of** [onFailure()] when the server responded
 *   with a non-2xx HTTP status AND a child event declares a matching `onNetworkFailure(httpCode)`
 *   trigger for that status code; screen state is still set to `Failure`; incomingData becomes
 *   the `NetworkResponseException`.
 *
 * **Failure scenarios:**
 * - Non-2xx HTTP response with matching child trigger: fires ONLY [onNetworkFailure(httpCode)]; screen state → `Failure`.
 * - Non-2xx HTTP response without matching child trigger: fires ONLY [onFailure()]; screen state → `Failure`.
 * - Network/IO exception or deserialization error: fires ONLY [onFailure()]; screen state → `Failure`.
 *
 * **Notes:**
 * - The screen is unconditionally reset to `Initial` state before the request begins — implicit
 *   loading signal without an explicit [onStart()].
 * - Screen ID is resolved at runtime from `EventRunningScope.screenId`.
 * - A chained [ChangeScreenStateEventSchema] is NOT needed; state is applied internally.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
        OnNetworkFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("RefreshScreen")
data class RefreshScreenEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("method") val method: HttpMethod = HttpMethod.GET,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?,
) : EventSchema