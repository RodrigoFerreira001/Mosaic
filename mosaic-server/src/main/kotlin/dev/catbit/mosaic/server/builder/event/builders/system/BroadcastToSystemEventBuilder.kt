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

fun incomingBroadcastData() = BroadcastData.Incoming
fun inlineBroadcastData(data: AnySerializable) = BroadcastData.Inline(data)
