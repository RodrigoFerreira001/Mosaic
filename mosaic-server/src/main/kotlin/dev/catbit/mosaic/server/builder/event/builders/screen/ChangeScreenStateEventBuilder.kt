package dev.catbit.mosaic.server.builder.event.builders.screen

import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema.State
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema.State.Success.ScreenData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class ChangeScreenStateEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val state: State
) : EventSchemaBuilder<ChangeScreenStateEventSchema>() {

    override fun build() = ChangeScreenStateEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        state = state
    )
}

fun EventSchemaBuilderScope.ChangeScreenState(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    state: State
) {
    addBuilder(
        ChangeScreenStateEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            state = state
        )
    )
}

fun initialState() = State.Initial
fun failureState() = State.Failure
fun successState(
    data: ScreenData? = null
) = State.Success(data)
