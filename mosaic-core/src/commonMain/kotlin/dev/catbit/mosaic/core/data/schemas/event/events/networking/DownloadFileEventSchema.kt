package dev.catbit.mosaic.core.data.schemas.event.events.networking

import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadPartialEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Downloads a file from [url] via [method] and fires streaming progress triggers throughout the
 * transfer lifecycle.
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
 * - [OnDownloadPartialEventTrigger] — fired per chunk; incomingData becomes the `ByteArray`
 *   of that chunk.
 * - [OnDownloadFinishEventTrigger] — fired once transfer completes; incomingData becomes the
 *   full `ByteArray` of the downloaded file.
 * - [OnSuccessEventTrigger] — fired after a successful download completes.
 * - [OnDownloadFailureEventTrigger] — fired if the download fails at any point; incomingData
 *   becomes the `Throwable`.
 * - [OnFailureEventTrigger] — fired for general failures (e.g. pre-request setup errors);
 *   incomingData becomes the `Throwable`.
 *
 * **Failure scenarios:**
 * - Non-2xx HTTP status code: throws `NetworkResponseException`; fires [OnDownloadFailureEventTrigger].
 * - Any network/IO error during streaming: fires [OnDownloadFailureEventTrigger].
 *
 * **Notes:**
 * - [OnDownloadProgressEventTrigger] and [OnDownloadPartialEventTrigger] interleave freely;
 *   both fire per received chunk.
 */
@Triggers(
    [
        OnStartEventTrigger::class,
        OnDownloadProgressEventTrigger::class,
        OnDownloadPartialEventTrigger::class,
        OnDownloadFinishEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnDownloadFailureEventTrigger::class,
        OnFailureEventTrigger::class,
    ]
)
@Serializable
@SerialName("DownloadFile")
data class DownloadFileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("url") val url: String,
    @SerialName("method") val method: HttpMethod,
    @SerialName("body") val body: AnySerializable?,
    @SerialName("headers") val headers: Map<String, String>?
) : EventSchema