package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.events.data.TransformDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class TransformDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val template: AnySerializable
) : EventSchemaBuilder<TransformDataEventSchema>() {

    override fun build() = TransformDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        template = template
    )
}

fun EventSchemaBuilderScope.TransformData(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    template: AnySerializable
) {
    addBuilder(
        TransformDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            template = template
        )
    )
}

fun EventSchemaBuilderScope.TransformData(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    eventTemplate: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        TransformDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            template = run {
                val template = EventSchemaBuilderScope(snapshotLocals())
                    .apply(eventTemplate)
                    .build()

                if (template.size == 1) template.first() else template
            }
        )
    )
}
