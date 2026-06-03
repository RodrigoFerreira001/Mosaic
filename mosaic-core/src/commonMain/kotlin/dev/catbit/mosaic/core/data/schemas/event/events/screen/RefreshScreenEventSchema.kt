package dev.catbit.mosaic.core.data.schemas.event.events.screen

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
 * - [onFailure()] – network call failed; screen state is set to `Failure`; incomingData becomes
 *   the `Throwable`. Logged via `logError`.
 *
 * **Failure scenarios:**
 * - Any network/IO exception or deserialization error fires [onFailure()] and sets screen to `Failure`.
 *
 * **Notes:**
 * - The screen is unconditionally reset to `Initial` state before the request begins — implicit
 *   loading signal without an explicit [onStart()].
 * - Screen ID is resolved at runtime from `EventRunningScope.screenId`.
 * - A chained [ChangeScreenStateEventSchema] is NOT needed; state is applied internally.
 */
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("RefreshScreen")
data class RefreshScreenEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("method") val method: HttpMethod = HttpMethod.GET,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?,
) : EventSchema