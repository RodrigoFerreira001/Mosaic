package dev.catbit.mosaic.server.builder.event.builders.tiles

import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema.Update
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema.Update.UpdateData
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
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
    private val updateData: UpdateData
) : GenericBuilder<Update>() {

    override fun build() = Update(
        tileId = tileId,
        updateData = updateData
    )
}

class UpdateTilesUpdateBuilderScope : GenericBuilderScope<Update, UpdateTilesUpdateBuilder>() {

    fun update(
        tileId: String,
        updateData: UpdateData
    ) {
        addBuilder(
            UpdateTilesUpdateBuilder(
                tileId = tileId,
                updateData = updateData
            )
        )
    }
}

fun incomingTileUpdateData() = UpdateData.Incoming
fun inlineTileUpdateData(data: Map<String, AnySerializable?>) = UpdateData.Inline(data)
fun inlineTileUpdateData(vararg data: Pair<String, AnySerializable?>) = UpdateData.Inline(data.toMap())
