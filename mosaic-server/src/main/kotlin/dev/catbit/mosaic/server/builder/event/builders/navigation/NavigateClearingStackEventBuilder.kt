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
