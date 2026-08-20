package dev.catbit.mosaic.server.builder.event.builders.event

import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema.Update
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class UpdateEventsEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val updates: UpdateEventsUpdateBuilderScope.() -> Unit
) : EventSchemaBuilder<UpdateEventsEventSchema>() {

    override fun build() = UpdateEventsEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        updates = UpdateEventsUpdateBuilderScope().apply(updates).build()
    )
}

/**
 * Patches events already registered on the screen. Each [updates] entry (built with `update`)
 * targets an event by id and merges a data map into it, the same way `UpdateTiles` patches a
 * tile, so a chain can rewrite another event's parameters before it runs. All updates are
 * attempted even if one fails. Does not consume `incomingData`. Dispatches `onSuccess` (no data)
 * when every update was applied; `onFailure` (no data), once at the end, when at least one failed
 * — typically because no event carries that id.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param updates Target-event patches, declared with `update`.
 */
fun EventSchemaBuilderScope.UpdateEvents(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    updates: UpdateEventsUpdateBuilderScope.() -> Unit
) {
    addBuilder(
        UpdateEventsEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            updates = updates
        )
    )
}

class UpdateEventsUpdateBuilder(
    private val eventId: String,
    private val data: Map<String, AnySerializable?>
) : GenericBuilder<Update>() {

    override fun build() = Update(
        eventId = eventId,
        data = data
    )
}

class UpdateEventsUpdateBuilderScope : GenericBuilderScope<Update, UpdateEventsUpdateBuilder>() {

    /**
     * Declares one patch inside an `UpdateEvents` event.
     *
     * @param eventId Id of the target event to patch.
     * @param data Key-value pairs merged into the target event's own parameters.
     */
    fun update(
        eventId: String,
        data: Map<String, AnySerializable?>
    ) {
        addBuilder(
            UpdateEventsUpdateBuilder(
                eventId = eventId,
                data = data
            )
        )
    }
}
