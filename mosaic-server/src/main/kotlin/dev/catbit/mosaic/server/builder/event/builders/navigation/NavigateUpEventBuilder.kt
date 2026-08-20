package dev.catbit.mosaic.server.builder.event.builders.navigation

import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateUpEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class NavigateUpEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val navigatorId: String
) : EventSchemaBuilder<NavigateUpEventSchema>() {

    override fun build() = NavigateUpEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        navigatorId = navigatorId
    )
}

/**
 * Pops the back stack of the graph registered under [navigatorId], going back one entry. Does
 * not consume `incomingData`. Dispatches `onSuccess` (no data) when an entry was popped;
 * `onFailure` (no data, logged) when no navigator is registered under [navigatorId] or there was
 * nothing to pop.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param navigatorId Id of the graph (`Graph`/`NestedNavigationGraph`) to pop.
 */
fun EventSchemaBuilderScope.NavigateUp(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    navigatorId: String
) {
    addBuilder(
        NavigateUpEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            navigatorId = navigatorId
        )
    )
}
