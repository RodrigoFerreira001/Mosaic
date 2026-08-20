package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.events.data.ProcessDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ProcessDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val processWith: String
) : EventSchemaBuilder<ProcessDataEventSchema>() {

    override fun build() = ProcessDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        processWith = processWith
    )
}

/**
 * Hands `incomingData` to the `DataProcessor` the host client app registered under [processWith]
 * — what the processing does is opaque to the framework, since processors are supplied by the
 * client. `incomingData` is required; the processor's own result is not passed downstream.
 * Dispatches `onSuccess` (no data) when the processor returns successfully; `onFailure` when the
 * processor returns a failure (carrying the `Throwable`), when no processor is registered under
 * [processWith], or when `incomingData` is `null` (no data in the latter two cases).
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param processWith Id of the client-registered `DataProcessor` to run `incomingData` through.
 */
fun EventSchemaBuilderScope.ProcessData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    processWith: String
) {
    addBuilder(
        ProcessDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            processWith = processWith
        )
    )
}
