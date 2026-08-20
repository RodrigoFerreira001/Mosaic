package dev.catbit.mosaic.server.builder.event.builders.screen

import dev.catbit.mosaic.core.data.schemas.event.events.screen.RefreshScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class RefreshScreenEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val method: HttpMethod,
    private val body: AnySerializable?,
    private val headers: Map<String, String>?,
    private val timeoutMillis: Long?,
) : EventSchemaBuilder<RefreshScreenEventSchema>() {

    override fun build() = RefreshScreenEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        method = method,
        body = body,
        headers = headers,
        timeoutMillis = timeoutMillis,
    )
}

/**
 * Refetches the screen this event lives in and applies the result to it directly — the screen
 * moves to its initial (loading) state, then to success with the new content or to its failure
 * state, unlike `GetScreen`, which needs a separate event to install what came back. The request
 * targets the screen's own id, shaped by [method], [body], [headers] and [timeoutMillis]. Does
 * not consume `incomingData`. Dispatches `onSuccess` (carrying the fetched `ScreenModel`) when the
 * screen was fetched and applied; `onNetworkFailure(status)` (carrying the failure) when the
 * request failed with an HTTP status and a child event is wired to that exact status; `onFailure`
 * (carrying the `Throwable`, logged) for every other failure and for HTTP failures with no
 * status-specific event declared — only one of the latter two fires per failure, and the screen
 * is left in its failure state either way.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onNetworkFailure`, `onFailure`).
 * @param method HTTP method used for the request. Defaults to GET.
 * @param body Request body. Defaults to none.
 * @param headers Request headers. Defaults to none.
 * @param timeoutMillis Request timeout, in milliseconds. Defaults to none (client default).
 */
fun EventSchemaBuilderScope.RefreshScreen(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    method: HttpMethod = HttpMethod.GET,
    body: AnySerializable? = null,
    headers: Map<String, String>? = null,
    timeoutMillis: Long? = null,
) {
    addBuilder(
        RefreshScreenEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            method = method,
            body = body,
            headers = headers,
            timeoutMillis = timeoutMillis,
        )
    )
}
