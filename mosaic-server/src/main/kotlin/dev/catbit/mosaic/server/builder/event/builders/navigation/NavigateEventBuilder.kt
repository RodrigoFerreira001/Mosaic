package dev.catbit.mosaic.server.builder.event.builders.navigation

import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateEventSchema.PopUpTo
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class NavigateEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val destination: String,
    private val navigatorId: String,
    private val popUpTo: PopUpTo? = null,
    private val data: Map<String, AnySerializable>? = null,
) : EventSchemaBuilder<NavigateEventSchema>() {

    override fun build() = NavigateEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        destination = destination,
        navigatorId = navigatorId,
        popUpTo = popUpTo,
        data = data,
    )
}

/**
 * Navigates the graph registered under [navigatorId] to [destination], pushing it onto the back
 * stack. The destination receives `incomingData` merged with [data] (with [data] winning on key
 * collision) as its navigation data — only map-shaped `incomingData` contributes, and `null`
 * values are dropped from both, since navigation arguments are never null. When [popUpTo] is set
 * (built with [poppingUpTo]), entries are popped up to that destination before the new one is
 * pushed. Dispatches `onSuccess` (no data) when the navigation was performed; `onFailure` (no
 * data, logged) when no navigator is registered under [navigatorId] or it refused the navigation.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param destination Screen id to navigate to.
 * @param navigatorId Id of the graph (`Graph`/`NestedNavigationGraph`) to navigate.
 * @param popUpTo Back-stack entries to pop before pushing [destination], built with [poppingUpTo]. Defaults to none.
 * @param data Extra navigation data merged with `incomingData`, winning on key collision. Defaults to none.
 */
fun EventSchemaBuilderScope.Navigate(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    destination: String,
    navigatorId: String,
    popUpTo: PopUpTo? = null,
    data: Map<String, AnySerializable>? = null,
) {
    addBuilder(
        NavigateEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            destination = destination,
            navigatorId = navigatorId,
            popUpTo = popUpTo,
            data = data
        )
    )
}

/**
 * Pops back-stack entries up to [destination] before the new one is pushed, feeding a `Navigate`
 * event's `popUpTo`.
 *
 * @param destination Screen id to pop back to.
 * @param inclusive Whether [destination] itself is also popped.
 */
fun poppingUpTo(
    destination: String,
    inclusive: Boolean
) = PopUpTo(
    destination = destination,
    inclusive = inclusive
)