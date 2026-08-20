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

/**
 * Patches tiles in place — the primary way to change what is on screen without refetching it.
 * Each [updates] entry (built with `update`) targets a tile by id and merges a map of field
 * names into it: only the listed fields change, and `style` is merged rather than replaced. The
 * map comes from [incomingTileUpdateData] (`incomingData` used as-is when it is a map; skipped
 * and counted as a failure otherwise), [inlineTileUpdateData] (a literal map declared on the
 * event), or [mappedIncomingTileUpdateData] (one template per field, each resolved against
 * `incomingData` through the client's `TemplateProcessor`, so a payload can be reshaped per field
 * on the way in). All updates are attempted even if one fails. Consumes `incomingData` for the
 * incoming and mapped forms. Dispatches `onSuccess` (no data) when every update was applied;
 * `onFailure` (no data, each failure logged), once at the end, when at least one update failed —
 * no tile carries that id, a mapped template threw, or an incoming update got non-map
 * `incomingData`.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param updates Tile patches, declared with `update`.
 */
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

    /**
     * Declares one patch inside an `UpdateTiles` event.
     *
     * @param tileId Id of the target tile to patch.
     * @param updateData Fields to merge in, built with [incomingTileUpdateData], [inlineTileUpdateData] or [mappedIncomingTileUpdateData].
     */
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

/** Uses `incomingData` as-is as the field map — must be a map, or the update is skipped and counted as a failure. */
fun incomingTileUpdateData() = UpdateData.Incoming

/** Uses a literal map of field names to values as the patch. */
fun inlineTileUpdateData(data: Map<String, AnySerializable?>) = UpdateData.Inline(data)

/** Uses a literal set of field names to values as the patch. */
fun inlineTileUpdateData(vararg data: Pair<String, AnySerializable?>) = UpdateData.Inline(data.toMap())

/** Resolves one `<|path|>`-style template per field name against `incomingData`, through the client's `TemplateProcessor`. */
fun mappedIncomingTileUpdateData(patterns: Map<String, String>) = UpdateData.Mapped(patterns)

/** Resolves one `<|path|>`-style template per field name against `incomingData`, through the client's `TemplateProcessor`. */
fun mappedIncomingTileUpdateData(vararg patterns: Pair<String, String>) = UpdateData.Mapped(patterns.toMap())
