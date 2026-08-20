package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendNetworkRequestEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SendNetworkRequestEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val url: String,
    private val method: HttpMethod,
    private val body: AnySerializable?,
    private val headers: Map<String, String>?,
    private val timeoutMillis: Long?,
) : EventSchemaBuilder<SendNetworkRequestEventSchema>() {

    override fun build() = SendNetworkRequestEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        url = url,
        method = method,
        body = body,
        headers = headers,
        timeoutMillis = timeoutMillis,
    )
}

/**
 * Sends an HTTP request to [url] with [method], [headers], [body] and an optional
 * [timeoutMillis], and emits the response downstream — the general-purpose way to talk to a
 * backend from inside an event chain. A JSON response is parsed into plain maps, lists and
 * primitives (a JSON body that fails to parse yields `null`); anything else is passed through as
 * a raw `ByteArray`. Does not consume `incomingData`. Dispatches `onStart` before the request is
 * sent; `onNetworkResponse(status)`/`onNetworkFailure(status)` (carrying the parsed body) when a
 * child event is wired to that exact HTTP status — the former for 2xx, the latter for the rest;
 * `onSuccess` (carrying the parsed body) when the response is 2xx and no status-specific event
 * was declared; `onFailure` (carrying the parsed body, or the `Throwable` on transport failures
 * like timeout/no connectivity — logged) when the response isn't 2xx and no status-specific event
 * was declared. Exactly one outcome trigger fires per response.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onStart`, `onNetworkResponse`, `onNetworkFailure`, `onSuccess`, `onFailure`).
 * @param url URL requested.
 * @param method HTTP method used for the request.
 * @param body Request body. Defaults to none.
 * @param headers Request headers. Defaults to none.
 * @param timeoutMillis Request timeout, in milliseconds. Defaults to none (client default).
 */
fun EventSchemaBuilderScope.SendNetworkRequest(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    url: String,
    method: HttpMethod,
    body: AnySerializable? = null,
    headers: Map<String, String>? = null,
    timeoutMillis: Long? = null,
) {
    addBuilder(
        SendNetworkRequestEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            url = url,
            method = method,
            body = body,
            headers = headers,
            timeoutMillis = timeoutMillis,
        )
    )
}
