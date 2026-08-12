package dev.catbit.mosaic.server.builder.event.builders.overlays.modal_bottom_sheet

import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DismissModalBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DismissModalBottomSheetEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val modalBottomSheetId: String
) : EventSchemaBuilder<DismissModalBottomSheetEventSchema>() {

    override fun build() = DismissModalBottomSheetEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        modalBottomSheetId = modalBottomSheetId
    )
}

fun EventSchemaBuilderScope.DismissModalBottomSheet(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    modalBottomSheetId: String
) {
    addBuilder(
        DismissModalBottomSheetEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            modalBottomSheetId = modalBottomSheetId
        )
    )
}
