package dev.catbit.mosaic.server.builder.event.builders.navigation

import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateClearingStackEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class NavigateClearingStackEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val destination: String,
    private val navigatorId: String,
    private val launchSingleTop: Boolean = true,
    private val data: Map<String, AnySerializable>? = null,
) : EventSchemaBuilder<NavigateClearingStackEventSchema>() {

    override fun build() = NavigateClearingStackEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        destination = destination,
        navigatorId = navigatorId,
        launchSingleTop = launchSingleTop,
        data = data,
    )
}

/**
 * Navigates the graph registered under [navigatorId] to [destination], clearing the whole back
 * stack so the destination becomes the only entry — the usual move after login or logout. The
 * destination receives `incomingData` merged with [data] (with [data] winning on key collision)
 * as its navigation data — only map-shaped `incomingData` contributes, and `null` values are
 * dropped from both, since navigation arguments are never null. [launchSingleTop] avoids stacking
 * a second copy when the destination is already the current entry. Dispatches `onSuccess` (no
 * data) when the navigation was performed; `onFailure` (no data, logged) when no navigator is
 * registered under [navigatorId] or it refused the navigation.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param destination Screen id to navigate to.
 * @param navigatorId Id of the graph (`Graph`/`NestedNavigationGraph`) to navigate.
 * @param launchSingleTop Whether to avoid stacking a duplicate when [destination] is already the current entry. Defaults to true.
 * @param data Extra navigation data merged with `incomingData`, winning on key collision. Defaults to none.
 */
fun EventSchemaBuilderScope.NavigateClearingStack(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    destination: String,
    navigatorId: String,
    launchSingleTop: Boolean = true,
    data: Map<String, AnySerializable>? = null,
) {
    addBuilder(
        NavigateClearingStackEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            destination = destination,
            navigatorId = navigatorId,
            launchSingleTop = launchSingleTop,
            data = data
        )
    )
}
