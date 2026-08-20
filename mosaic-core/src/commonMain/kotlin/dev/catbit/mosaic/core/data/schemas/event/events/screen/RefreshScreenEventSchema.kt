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
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Refetches the screen this event lives in and applies the result to it. The screen is moved to
 * its initial (loading) state, then to success with the new content or to its failure state —
 * unlike `GetScreen`, no extra event is needed to install what came back.
 *
 * The request targets the screen's own id; [method] (default `GET`), [body], [headers] and
 * [timeoutMillis] shape it.
 *
 * **incomingData consumed:** not used.
 *
 * **Triggers fired:**
 * - `OnSuccessEventTrigger` — when the screen was fetched and applied; the `ScreenModel` is passed
 *   as incomingData.
 * - `OnNetworkFailureEventTrigger` — when the request failed with an HTTP status **and** this
 *   event declares an event wired to that exact status; the failure is passed as incomingData.
 * - `OnFailureEventTrigger` — every other failure, and also HTTP failures with no status-specific
 *   event declared; the `Throwable` is passed as incomingData. Only one of this and the trigger
 *   above fires per failure. Failures are logged, and the screen is left in its failure state
 *   either way.
 */
@Immutable
@Triggers(
    [
        OnSuccessEventTrigger::class,
        OnNetworkFailureEventTrigger::class,
        OnFailureEventTrigger::class,
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
    @SerialName("timeoutMillis") val timeoutMillis: Long? = null
) : EventSchema