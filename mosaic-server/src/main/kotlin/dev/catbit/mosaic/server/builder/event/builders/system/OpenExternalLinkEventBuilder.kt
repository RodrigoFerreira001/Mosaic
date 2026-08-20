package dev.catbit.mosaic.server.builder.event.builders.system

import dev.catbit.mosaic.core.data.schemas.event.events.system.OpenExternalLinkEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class OpenExternalLinkEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val url: String,
) : EventSchemaBuilder<OpenExternalLinkEventSchema>() {

    override fun build() = OpenExternalLinkEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        url = url,
    )
}

/**
 * Hands [url] to the platform so it opens outside the app — the system browser, or whichever app
 * claims the scheme. Does not consume `incomingData`. Dispatches `onSuccess` (no data) after the
 * platform accepted the request; `onFailure` (carrying the thrown exception, logged) when opening
 * throws, e.g. because nothing on the device handles the URL.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param url URL to open outside the app.
 */
fun EventSchemaBuilderScope.OpenExternalLink(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    url: String,
) {
    addBuilder(
        OpenExternalLinkEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            url = url,
        )
    )
}
