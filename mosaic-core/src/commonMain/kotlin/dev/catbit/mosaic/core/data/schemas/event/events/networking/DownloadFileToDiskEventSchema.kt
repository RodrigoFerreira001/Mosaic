package dev.catbit.mosaic.core.data.schemas.event.events.networking

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Downloads a file from [url] via [method] straight to local storage at [targetFileName],
 * streaming the response body to disk chunk by chunk without ever holding the full file in
 * memory. Use this instead of [DownloadFileEventSchema] whenever the downloaded content only
 * needs to be persisted (e.g. caching an asset for offline use), not processed as bytes.
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
 * - [OnStartEventTrigger] — immediately before the download request is dispatched.
 * - [OnDownloadProgressEventTrigger] — fired per chunk; incomingData becomes an `Int` 0–100
 *   representing completion percentage (only when `Content-Length` is available and > 0).
 * - [OnDownloadFinishEventTrigger] — fired once the file is fully written to disk; incomingData
 *   becomes [targetFileName] (a `String`), so downstream events can read it back (e.g. via
 *   `GetFile`).
 * - [OnSuccessEventTrigger] — fired after a successful download completes; incomingData becomes
 *   [targetFileName].
 * - [OnDownloadFailureEventTrigger] — fired if the download fails at any point; incomingData
 *   becomes the `Throwable`.
 * - [OnFailureEventTrigger] — fired for general failures (e.g. pre-request setup errors);
 *   incomingData becomes the `Throwable`.
 *
 * **Failure scenarios:**
 * - Non-2xx HTTP status code: throws `NetworkResponseException`; fires [OnDownloadFailureEventTrigger].
 * - Any network/IO error during streaming, or failure writing to disk: fires [OnDownloadFailureEventTrigger].
 *
 * **Notes:**
 * - [targetFileName] identifies the destination file by name within the app's private storage
 *   scope (e.g. `"models/abc123.glb"`).
 */
@Immutable
@Triggers(
    [
        OnStartEventTrigger::class,
        OnDownloadProgressEventTrigger::class,
        OnDownloadFinishEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnDownloadFailureEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("DownloadFileToDisk")
data class DownloadFileToDiskEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("url") val url: String,
    @SerialName("method") val method: HttpMethod,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?,
    @SerialName("targetFileName") val targetFileName: String
) : EventSchema
