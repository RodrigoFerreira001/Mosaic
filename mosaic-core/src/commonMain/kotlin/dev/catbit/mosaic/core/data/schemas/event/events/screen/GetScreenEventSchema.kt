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
 * - [onSuccess()] – server returned a valid `ScreenModel`; incomingData becomes the `ScreenModel`.
 * - [onFailure()] – network call failed; incomingData becomes the `Throwable`. Logged via `logError`.
 *
 * **Failure scenarios:**
 * - Any network/IO exception or deserialization error fires [onFailure()] with the `Throwable`.
 *
 * **Notes:**
 * - Unlike [RefreshScreenEventSchema], this event does NOT automatically apply the fetched
 *   `ScreenModel` to the screen state. Chain a [ChangeScreenStateEventSchema] on [onSuccess()]
 *   to apply the result.
 * - No [onStart()] trigger is fired.
 */
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class
    ]
)
@Serializable
@SerialName("GetScreen")
data class GetScreenEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("method") val method: HttpMethod = HttpMethod.GET,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?,
) : EventSchema