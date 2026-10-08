package dev.catbit.mosaic.server.builder.event.builders.screen

import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.network.HttpMethod
import dev.catbit.mosaic.core.data.schemas.network.TimeoutsSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.event.builders.networking.httpGet
import dev.catbit.mosaic.server.builder.event.builders.networking.timeout

internal class GetScreenEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val method: HttpMethod,
    private val body: AnySerializable?,
    private val headers: Map<String, String>?,
    private val timeouts: TimeoutsSchema?,
) : EventSchemaBuilder<GetScreenEventSchema>() {

    override fun build() = GetScreenEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        method = method,
        body = body,
        headers = headers,
        timeouts = timeouts,
    )
}

/**
 * Fetches the payload of the screen this event lives in and emits it downstream, without
 * applying it — pair it with `ChangeScreenState` to decide when and how the fetched content is
 * installed, which is what makes custom loading and error flows possible. The request targets
 * the screen's own id, shaped by [method], [body], [headers] and [timeouts]. Does not
 * consume `incomingData`. Dispatches `onStart` before the request is sent; `onSuccess` (carrying
 * the fetched `ScreenModel`, ready for `ChangeScreenState`) when the screen was fetched;
 * `onNetworkFailure(status)` (carrying the failure) when the request failed with an HTTP status
 * and a child event is wired to that exact status; `onFailure` (carrying the `Throwable`, logged)
 * for every other failure and for HTTP failures with no status-specific event declared — only one
 * of the latter two fires per failure.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onStart`, `onSuccess`, `onNetworkFailure`, `onFailure`).
 * @param method HTTP method used for the request, built with [httpGet], [httpPost], [httpPut], [httpDelete], [httpPatch] etc. Defaults to GET.
 * @param body Request body. Defaults to none.
 * @param headers Request headers. Defaults to none.
 * @param timeouts Request, connect and socket timeout overrides, built with [timeout]. Defaults to none (client defaults).
 */
fun EventSchemaBuilderScope.GetScreen(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    method: HttpMethod = httpGet(),
    body: AnySerializable? = null,
    headers: Map<String, String>? = null,
    timeouts: TimeoutsSchema? = null,
) {
    addBuilder(
        GetScreenEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            method = method,
            body = body,
            headers = headers,
            timeouts = timeouts,
        )
    )
}
