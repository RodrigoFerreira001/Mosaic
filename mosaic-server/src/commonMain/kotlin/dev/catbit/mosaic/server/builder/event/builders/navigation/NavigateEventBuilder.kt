package dev.catbit.mosaic.server.builder.event.builders.navigation

import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateEventSchema.PopUpTo
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
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
) : EventSchemaBuilder<NavigateEventSchema> {

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

fun poppingUpTo(
    destination: String,
    inclusive: Boolean
) = PopUpTo(
    destination = destination,
    inclusive = inclusive
)

fun EventSchemaBuilderScope.Navigate(
    id: String = randomUuid(),
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