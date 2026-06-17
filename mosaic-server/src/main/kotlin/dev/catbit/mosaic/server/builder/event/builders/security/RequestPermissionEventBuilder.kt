package dev.catbit.mosaic.server.builder.event.builders.security

import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema.Permissions
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import kotlinx.collections.immutable.toImmutableList

internal class RequestPermissionEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val permissions: List<Permissions>
) : EventSchemaBuilder<RequestPermissionEventSchema>() {

    override fun build() = RequestPermissionEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        permissions = permissions.toImmutableList()
    )
}

fun EventSchemaBuilderScope.RequestPermission(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    permissions: List<Permissions>
) {
    addBuilder(
        RequestPermissionEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            permissions = permissions
        )
    )
}
