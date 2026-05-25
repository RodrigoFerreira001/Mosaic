package dev.catbit.mosaic.server.builder.event.builders.menu

import dev.catbit.mosaic.core.data.schemas.event.events.menu.ToggleMenuEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ToggleMenuEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val menuId: String
) : EventSchemaBuilder<ToggleMenuEventSchema>() {

    override fun build() = ToggleMenuEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        menuId = menuId
    )
}

fun EventSchemaBuilderScope.ToggleMenu(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    menuId: String
) {
    addBuilder(
        ToggleMenuEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            menuId = menuId
        )
    )
}
