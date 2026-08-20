package dev.catbit.mosaic.server.builder.event.builders.screen

import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema.State
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema.State.Success.ScreenData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
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

/**
 * Moves the screen this event lives in to another [state] — [initialState] (its loading state),
 * [failureState] (its failure state), or [successState] with the content to display. For success,
 * the content comes from the data passed to [successState] when set, otherwise from
 * `incomingData`, which must then be a `ScreenModel` — typically the one produced by a preceding
 * `GetScreen`. Dispatches `onSuccess` (no data) when the state was applied; `onFailure` (no data,
 * logged) when applying it throws, including a success state with no declared data and no
 * `ScreenModel` in `incomingData`.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param state Target screen state, built with [initialState], [failureState] or [successState].
 */
fun EventSchemaBuilderScope.ChangeScreenState(
    id: String = randomId(),
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

/** Screen's loading state — its `initialTiles`/`initialEvents` are shown. */
fun initialState() = State.Initial

/** Screen's failure state — its `failureTiles`/`failureEvents` are shown. */
fun failureState() = State.Failure

/** Screen's success state, showing [data]'s content — or, when `null`, `incomingData` as the `ScreenModel` instead. */
fun successState(
    data: ScreenData? = null
) = State.Success(data)
