package dev.catbit.mosaic.server.builder.event.builders.system

import dev.catbit.mosaic.core.data.schemas.event.events.system.DropCachesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class DropCachesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val dropScreensCache: Boolean,
    private val dropInitialGraphCache: Boolean,
    private val dropVersionCache: Boolean,
) : EventSchemaBuilder<DropCachesEventSchema>() {

    override fun build() = DropCachesEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        dropScreensCache = dropScreensCache,
        dropInitialGraphCache = dropInitialGraphCache,
        dropVersionCache = dropVersionCache,
    )
}

fun EventSchemaBuilderScope.DropCaches(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    dropScreensCache: Boolean,
    dropInitialGraphCache: Boolean,
    dropVersionCache: Boolean,
) {
    addBuilder(
        DropCachesEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            dropScreensCache = dropScreensCache,
            dropInitialGraphCache = dropInitialGraphCache,
            dropVersionCache = dropVersionCache,
        )
    )
}
