package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema.Update
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class UpdateTilesEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val updates: UpdateTilesUpdateBuilderScope.() -> Unit
) : EventSchemaBuilder<UpdateTilesEventSchema>() {

    override fun build() = UpdateTilesEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        updates = UpdateTilesUpdateBuilderScope().apply(updates).build()
    )
}

fun EventSchemaBuilderScope.UpdateTiles(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    updates: UpdateTilesUpdateBuilderScope.() -> Unit
) {
    addBuilder(
        UpdateTilesEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            updates = updates
        )
    )
}

class UpdateTilesUpdateBuilder(
    private val tileId: String,
    private val data: Map<String, AnySerializable?>
) : GenericBuilder<Update>() {

    override fun build() = Update(
        tileId = tileId,
        data = data
    )
}

class UpdateTilesUpdateBuilderScope private constructor(): GenericBuilderScope<Update, UpdateTilesUpdateBuilder>() {

    companion object {
        internal operator fun invoke(
            compositionLocals: Map<CompositionLocal<*>, ValueProvider<*>>
        ) = UpdateTilesUpdateBuilderScope().apply { pushLocals(compositionLocals) }
    }

    fun update(
        tileId: String,
        data: Map<String, AnySerializable?>
    ) {
        addBuilder(
            UpdateTilesUpdateBuilder(
                tileId = tileId,
                data = data
            )
        )
    }
}
