package dev.catbit.mosaic.server.builder.event.builders.overlays.bottom_sheet

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DismissBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DismissBottomSheetEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<DismissBottomSheetEventSchema>() {

    override fun build() = DismissBottomSheetEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

fun EventSchemaBuilderScope.DismissBottomSheet(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        DismissBottomSheetEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
