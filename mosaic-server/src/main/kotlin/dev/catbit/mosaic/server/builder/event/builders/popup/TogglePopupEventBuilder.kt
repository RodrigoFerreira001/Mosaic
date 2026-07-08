package dev.catbit.mosaic.server.builder.event.builders.popup

import dev.catbit.mosaic.core.data.schemas.event.events.popup.TogglePopupEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class TogglePopupEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val popupId: String
) : EventSchemaBuilder<TogglePopupEventSchema>() {

    override fun build() = TogglePopupEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        popupId = popupId
    )
}

fun EventSchemaBuilderScope.TogglePopup(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    popupId: String
) {
    addBuilder(
        TogglePopupEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            popupId = popupId
        )
    )
}
