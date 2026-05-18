package dev.catbit.mosaic.server.builder.event.builders.overlays.dialog

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DismissDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DismissDialogEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<DismissDialogEventSchema>() {

    override fun build() = DismissDialogEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

fun EventSchemaBuilderScope.DismissDialog(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        DismissDialogEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
