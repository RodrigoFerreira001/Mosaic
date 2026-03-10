package dev.catbit.mosaic.server.builder.event.builders.time

import dev.catbit.mosaic.core.data.schemas.event.events.time.StartCountdownTimerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class StartCountdownTimerEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val setupTimeInSeconds: Long
) : EventSchemaBuilder<StartCountdownTimerEventSchema> {

    override fun build() = StartCountdownTimerEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        setupTimeInSeconds = setupTimeInSeconds
    )
}

fun EventSchemaBuilderScope.StartCountdownTimer(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    setupTimeInSeconds: Long
) {
    addBuilder(
        StartCountdownTimerEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            setupTimeInSeconds = setupTimeInSeconds
        )
    )
}
