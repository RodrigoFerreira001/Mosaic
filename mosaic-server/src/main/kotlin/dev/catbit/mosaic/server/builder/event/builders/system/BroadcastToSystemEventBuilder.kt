package dev.catbit.mosaic.server.builder.event.builders.system

import dev.catbit.mosaic.core.data.schemas.event.events.system.BroadcastToSystemEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.system.BroadcastToSystemEventSchema.BroadcastData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class BroadcastToSystemEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val broadcastId: String,
    private val data: BroadcastData
) : EventSchemaBuilder<BroadcastToSystemEventSchema>() {

    override fun build() = BroadcastToSystemEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        broadcastId = broadcastId,
        data = data
    )
}

/**
 * Publishes a value on the client's system broadcast channel under [broadcastId], where the host
 * application and any mounted `SystemBroadcastListener` tile can pick it up — the outbound half
 * of the bridge between server-declared flows and native app code. [data] chooses the payload:
 * [incomingBroadcastData] publishes `incomingData`, [inlineBroadcastData] publishes a literal
 * declared on the event. Consumes `incomingData` as the payload when [data] is
 * [incomingBroadcastData]. Dispatches `onSuccess` (no data) after the value was published;
 * `onFailure` (no data), only in the incoming-data case, when `incomingData` is `null` so there
 * is nothing to publish.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param broadcastId Channel id the value is published under, matched by `onSystemBroadcast(broadcastId)` listeners.
 * @param data Payload to publish, built with [incomingBroadcastData] or [inlineBroadcastData].
 */
fun EventSchemaBuilderScope.BroadcastToSystem(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    broadcastId: String,
    data: BroadcastData
) {
    addBuilder(
        BroadcastToSystemEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            broadcastId = broadcastId,
            data = data
        )
    )
}

/** Publishes `incomingData` as the broadcast payload. */
fun incomingBroadcastData() = BroadcastData.Incoming

/** Publishes a literal [data] value as the broadcast payload. */
fun inlineBroadcastData(data: AnySerializable) = BroadcastData.Inline(data)
