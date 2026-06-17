package dev.catbit.mosaic.server.builder.event.builders.networking

import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderUrlEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class SetIncomingDataToNetworkParamsHolderUrlEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {}
) : EventSchemaBuilder<SetIncomingDataToNetworkParamsHolderUrlEventSchema>() {

    override fun build() = SetIncomingDataToNetworkParamsHolderUrlEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build()
    )
}

fun EventSchemaBuilderScope.SetIncomingDataToNetworkParamsHolderUrl(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {}
) {
    addBuilder(
        SetIncomingDataToNetworkParamsHolderUrlEventBuilder(
            id = id,
            trigger = trigger,
            events = events
        )
    )
}
