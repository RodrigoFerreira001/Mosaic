package dev.catbit.mosaic.server.builder.event.builders.system

import dev.catbit.mosaic.core.data.schemas.event.events.system.OpenExternalLinkEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class OpenExternalLinkEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val url: String,
) : EventSchemaBuilder<OpenExternalLinkEventSchema>() {

    override fun build() = OpenExternalLinkEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        url = url,
    )
}

fun EventSchemaBuilderScope.OpenExternalLink(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    url: String,
) {
    addBuilder(
        OpenExternalLinkEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            url = url,
        )
    )
}
