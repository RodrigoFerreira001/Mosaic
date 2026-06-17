package dev.catbit.mosaic.core.data.schemas.event.events.networking

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.annotations.Triggers
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkResponseTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUploadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Uploads a file as a raw binary body (no multipart) to [url] via [method], firing progress
 * triggers throughout the transfer. Designed for the **signed URL** pattern: the application
 * backend issues a temporary upload URL (GCS/S3) and the client sends the bytes directly to the
 * storage, so large files never pass through the application server.
 *
 * **incomingData consumed:** The file bytes as a `ByteArray` (e.g. produced by
 * [DownloadFileEventSchema]'s OnDownloadFinish or a binary [SendNetworkRequestEventSchema]
 * response). A non-`ByteArray` or `null` incomingData fires [OnFailureEventTrigger] without
 * making any request.
 *
 * **URL resolution:** schema [url] ?? holder.url (set by
 * [SetIncomingDataToNetworkParamsHolderUrlEventSchema] earlier in the chain — the typical way to
 * feed a runtime-generated signed URL). If both are `null`, fires [OnFailureEventTrigger] with
 * `MissingUploadUrlException`.
 *
 * **Headers resolution:** holder.headers + schema.headers (schema takes precedence on collision).
 * The holder is always consumed on execution.
 *
 * **Content type:** [contentType] is applied to the binary body
 * (default `application/octet-stream`). For signed URLs it must match the content type used when
 * signing, or the storage will reject the request.
 *
 * **Triggers fired:**
 * - [OnStartEventTrigger] — immediately before the upload request is dispatched.
 * - [OnUploadProgressEventTrigger] — fired when the sent percentage changes; incomingData
 *   becomes an `Int` 0–100.
 * - [OnSuccessEventTrigger] — 2xx response when **no** child event declares a matching
 *   `onNetworkResponse(statusCode)` or `onNetworkFailure(statusCode)` trigger for that status;
 *   incomingData becomes the response body (JSON → `Any`, otherwise `ByteArray`).
 * - [OnFailureEventTrigger] — non-2xx response without a matching custom listener, network
 *   error, missing URL, or invalid incomingData; incomingData becomes the response body or
 *   the `Throwable`.
 * - [OnNetworkResponseTrigger] — fired **instead of** [OnSuccessEventTrigger] when a child
 *   event declares a matching trigger for a **2xx** status code; incomingData becomes the
 *   response body.
 * - [OnNetworkFailureEventTrigger] — fired **instead of** [OnFailureEventTrigger] when a child
 *   event declares a matching trigger for a **non-2xx** status code; incomingData becomes the
 *   response body. Never fired on network/IO exceptions.
 *
 * A child event activates custom dispatch for a given status code if it declares either
 * `onNetworkResponse(statusCode)` **or** `onNetworkFailure(statusCode)` for that code.
 */
@Immutable
@Triggers(
    [
        OnStartEventTrigger::class,
        OnUploadProgressEventTrigger::class,
        OnSuccessEventTrigger::class,
        OnFailureEventTrigger::class,
        OnNetworkResponseTrigger::class
    ]
)
@Serializable
@SerialName("SendFile")
data class SendFileEventSchema(
    @SerialName("id") override val id: String,
    @SerialName("trigger") override val trigger: EventTrigger,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("url") val url: String?,
    @SerialName("method") val method: HttpMethod,
    @SerialName("headers") val headers: Map<String, String>?,
    @SerialName("contentType") val contentType: String?
) : EventSchema
