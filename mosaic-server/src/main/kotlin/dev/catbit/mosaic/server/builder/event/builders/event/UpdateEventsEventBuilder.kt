package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema.Update
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class UpdateEventsEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val updates: UpdateEventsUpdateBuilderScope.() -> Unit
) : EventSchemaBuilder<UpdateEventsEventSchema>() {

    override fun build() = UpdateEventsEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        updates = UpdateEventsUpdateBuilderScope().apply(updates).build()
    )
}

fun EventSchemaBuilderScope.UpdateEvents(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    updates: UpdateEventsUpdateBuilderScope.() -> Unit
) {
    addBuilder(
        UpdateEventsEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            updates = updates
        )
    )
}

class UpdateEventsUpdateBuilder(
    private val eventId: String,
    private val data: Map<String, AnySerializable?>
) : GenericBuilder<Update>() {

    override fun build() = Update(
        eventId = eventId,
        data = data
    )
}

class UpdateEventsUpdateBuilderScope : GenericBuilderScope<Update, UpdateEventsUpdateBuilder>() {

    fun update(
        eventId: String,
        data: Map<String, AnySerializable?>
    ) {
        addBuilder(
            UpdateEventsUpdateBuilder(
                eventId = eventId,
                data = data
            )
        )
    }
}
