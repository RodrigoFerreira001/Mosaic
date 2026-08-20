package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DownloadFileEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val url: String,
    private val method: HttpMethod,
    private val body: AnySerializable?,
    private val headers: Map<String, String>?,
    private val targetFileName: String,
    private val mimeType: String?
) : EventSchemaBuilder<DownloadFileEventSchema>() {

    override fun build() = DownloadFileEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        url = url,
        method = method,
        body = body,
        headers = headers,
        targetFileName = targetFileName,
        mimeType = mimeType
    )
}

/**
 * Downloads [url] and hands the result to the platform's own download destination — the user's
 * Downloads folder or equivalent — under [targetFileName], with [mimeType] describing the
 * content. [method], [headers] and [body] shape the request. Does not consume `incomingData`.
 * Dispatches `onStart` before the download begins; `onDownloadProgress` repeatedly while
 * downloading (carrying the progress); `onDownloadFinish` then `onSuccess` (both carrying
 * [targetFileName]) when the download completed; `onDownloadFailure` then `onFailure` (both
 * carrying the `Throwable`, logged) when it failed; `onCancelled` (no data) when the user cancels
 * the download — in that case neither download-failure nor failure fires.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onStart`, `onDownloadProgress`, `onDownloadFinish`, `onDownloadFailure`, `onSuccess`, `onFailure`, `onCancelled`).
 * @param url URL to download.
 * @param method HTTP method used for the request.
 * @param body Request body. Defaults to none.
 * @param headers Request headers. Defaults to none.
 * @param targetFileName Name the downloaded file is saved under, in the platform's download destination.
 * @param mimeType MIME type describing the downloaded content. Defaults to none.
 */
fun EventSchemaBuilderScope.DownloadFile(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    url: String,
    method: HttpMethod,
    body: AnySerializable? = null,
    headers: Map<String, String>? = null,
    targetFileName: String,
    mimeType: String? = null
) {
    addBuilder(
        DownloadFileEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            url = url,
            method = method,
            body = body,
            headers = headers,
            targetFileName = targetFileName,
            mimeType = mimeType
        )
    )
}
