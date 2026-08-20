package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileToMemoryEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DownloadFileToMemoryEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val url: String,
    private val method: HttpMethod,
    private val body: AnySerializable?,
    private val headers: Map<String, String>?
) : EventSchemaBuilder<DownloadFileToMemoryEventSchema>() {

    override fun build() = DownloadFileToMemoryEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        url = url,
        method = method,
        body = body,
        headers = headers
    )
}

/**
 * Downloads [url] without touching the filesystem, keeping the content in memory. [method],
 * [headers] and [body] shape the request. Does not consume `incomingData`. Dispatches `onStart`
 * before the download begins; `onDownloadProgress` repeatedly while downloading (carrying the
 * progress); `onDownloadFinish` then `onSuccess` (both carrying the total byte count) when the
 * download completed; `onDownloadFailure` then `onFailure` (both carrying the `Throwable`,
 * logged) when it failed.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onStart`, `onDownloadProgress`, `onDownloadFinish`, `onDownloadFailure`, `onSuccess`, `onFailure`).
 * @param url URL to download.
 * @param method HTTP method used for the request.
 * @param body Request body. Defaults to none.
 * @param headers Request headers. Defaults to none.
 */
fun EventSchemaBuilderScope.DownloadFileToMemory(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    url: String,
    method: HttpMethod,
    body: AnySerializable? = null,
    headers: Map<String, String>? = null
) {
    addBuilder(
        DownloadFileToMemoryEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            url = url,
            method = method,
            body = body,
            headers = headers
        )
    )
}
