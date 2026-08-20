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
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUploadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Uploads the file carried in the event's incomingData to [url] with [method], [headers] and
 * [contentType], reporting progress as it goes. Pair it with `OpenFilePicker` or `GetFile` using
 * their `PlatformFile` output.
 *
 * **Response shape:** a JSON content type is parsed into plain maps, lists and primitives;
 * anything else is passed through as raw bytes. A JSON body that fails to parse yields `null`.
 *
 * **incomingData consumed:** required, and must be a `PlatformFile` — it is the file uploaded.
 *
 * **Triggers fired:**
 * - `OnStartEventTrigger` — after incomingData was validated, before the upload begins.
 * - `OnUploadProgressEventTrigger` — repeatedly while uploading, with the progress passed as
 *   incomingData.
 * - `OnNetworkResponseEventTrigger` / `OnNetworkFailureEventTrigger` — when this event declares an
 *   event wired to the response's exact HTTP status: the first for successful statuses, the second
 *   for the rest. The parsed body is passed as incomingData.
 * - `OnSuccessEventTrigger` — when the upload succeeded and no status-specific event was declared;
 *   the parsed body is passed as incomingData.
 * - `OnFailureEventTrigger` — when incomingData is not a `PlatformFile` (fired before
 *   `OnStartEventTrigger`, with no data), when the response is unsuccessful and no status-specific
 *   event was declared (the parsed body is passed), and when the upload itself fails (the
 *   `Throwable` is passed). Failures are logged.
 */
@Immutable
@Triggers(
    [
        OnStartEventTrigger::class,
        OnUploadProgressEventTrigger::class,
        OnNetworkResponseEventTrigger::class,
        OnNetworkFailureEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("UploadFile")
data class UploadFileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("url") val url: String?,
    @SerialName("method") val method: HttpMethod,
    @SerialName("headers") val headers: Map<String, String>?,
    @SerialName("contentType") val contentType: String?
) : EventSchema
