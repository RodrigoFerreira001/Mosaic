package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.events.data.SendDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SendDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val dataKey: String,
    private val data: AnySerializable?
) : EventSchemaBuilder<SendDataEventSchema>() {

    override fun build() = SendDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        dataKey = dataKey,
        data = data
    )
}

fun EventSchemaBuilderScope.SendData(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    dataKey: String,
    data: AnySerializable? = null
) {
    addBuilder(
        SendDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            dataKey = dataKey,
            data = data
        )
    )
}
