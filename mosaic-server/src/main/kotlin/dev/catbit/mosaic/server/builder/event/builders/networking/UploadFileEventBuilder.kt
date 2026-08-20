package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.UploadFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class UploadFileEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val url: String?,
    private val method: HttpMethod,
    private val headers: Map<String, String>?,
    private val contentType: String?,
) : EventSchemaBuilder<UploadFileEventSchema>() {

    override fun build() = UploadFileEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        url = url,
        method = method,
        headers = headers,
        contentType = contentType,
    )
}

/**
 * Uploads the `PlatformFile` carried in `incomingData` to [url] with [method], [headers] and
 * [contentType], reporting progress as it goes — pair it with `OpenFilePicker` or `GetFile` using
 * their platform-file output. A JSON response is parsed into plain maps, lists and primitives (a
 * JSON body that fails to parse yields `null`); anything else is passed through as raw bytes.
 * `incomingData` is required and must be a `PlatformFile`. Dispatches `onStart` after
 * `incomingData` is validated, before the upload begins; `onUploadProgress` repeatedly while
 * uploading (carrying the progress); `onNetworkResponse(status)`/`onNetworkFailure(status)`
 * (carrying the parsed body) when a child event is wired to that exact HTTP status — the former
 * for successful statuses, the latter for the rest; `onSuccess` (carrying the parsed body) when
 * the upload succeeded and no status-specific event was declared; `onFailure` when `incomingData`
 * isn't a `PlatformFile` (fired before `onStart`, no data), when the response is unsuccessful
 * with no status-specific event declared (carrying the parsed body), or when the upload itself
 * fails (carrying the `Throwable`) — all logged.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onStart`, `onUploadProgress`, `onNetworkResponse`, `onNetworkFailure`, `onSuccess`, `onFailure`).
 * @param url URL the file is uploaded to. Defaults to none (dispatches `onFailure` if left unset by the time the event runs — set via `SetIncomingDataToNetworkParamsHolderUrl` instead if omitted here).
 * @param method HTTP method used for the request. Defaults to PUT.
 * @param headers Request headers. Defaults to none.
 * @param contentType Content type of the uploaded file. Defaults to none.
 */
fun EventSchemaBuilderScope.UploadFile(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    url: String? = null,
    method: HttpMethod = HttpMethod.PUT,
    headers: Map<String, String>? = null,
    contentType: String? = null,
) {
    addBuilder(
        UploadFileEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            url = url,
            method = method,
            headers = headers,
            contentType = contentType,
        )
    )
}
