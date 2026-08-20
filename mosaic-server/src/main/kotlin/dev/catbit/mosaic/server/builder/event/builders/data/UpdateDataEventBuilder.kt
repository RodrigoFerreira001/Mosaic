package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.UpdateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.UpdateDataEventSchema.Update
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class UpdateDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val updates: UpdateDataUpdateBuilderScope.() -> Unit
) : EventSchemaBuilder<UpdateDataEventSchema>() {

    override fun build() = UpdateDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        updates = UpdateDataUpdateBuilderScope().apply(updates).build()
    )
}

/**
 * Writes values into data sources, on the IO dispatcher. Updates are grouped by data source, and
 * each [updates] entry (built with `update`) resolves to a set of key/value entries: incoming
 * data (built with [incomingUpdateData]) spreads `incomingData` when it is a map (and contributes
 * nothing when it isn't), inline data (built with [inlineUpdateData]) writes a literal map, and
 * explicit data (built with [explicitUpdateData]/[explicitIncomingUpdateData]/[explicitNullUpdateData])
 * writes a single known key taking its value from a literal or from `incomingData` as-is — the
 * only way to write a value that is itself a map/record intact. Supports the application and
 * screen data holders (plain and segmented) and the plain and segmented local databases; updates
 * targeting navigation data, a tile or an inline source are silently ignored. The in-memory data
 * holders accept `null` (clearing the entry); the databases don't yet, so entries resolving to
 * `null` are skipped instead of written. Dispatches `onSuccess` (no data) when every write
 * completes without error; `onFailure` (no data, each failure logged), once at the end, when at
 * least one database write failed.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onSuccess`, `onFailure`).
 * @param updates Data source writes, declared with `update`.
 */
fun EventSchemaBuilderScope.UpdateData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    updates: UpdateDataUpdateBuilderScope.() -> Unit
) {
    addBuilder(
        UpdateDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            updates = updates
        )
    )
}

class UpdateDataUpdateBuilder(
    private val dataSource: DataSourceSchema,
    private val updateData: Update.UpdateDate
) : GenericBuilder<Update>() {

    override fun build() = Update(
        dataSource = dataSource,
        updateData = updateData
    )
}

class UpdateDataUpdateBuilderScope : GenericBuilderScope<Update, UpdateDataUpdateBuilder>() {

    /**
     * Declares one write inside an `UpdateData` event.
     *
     * @param dataSource Data source to write to, built with the `data` package helpers (e.g. `plainDataBase()`, `segmentedDataBase(...)`).
     * @param updateData What to write, built with [incomingUpdateData], [inlineUpdateData], [explicitUpdateData], [explicitIncomingUpdateData] or [explicitNullUpdateData].
     */
    fun update(
        dataSource: DataSourceSchema,
        updateData: Update.UpdateDate
    ) {
        addBuilder(
            UpdateDataUpdateBuilder(
                dataSource = dataSource,
                updateData = updateData,
            )
        )
    }
}

/** Spreads `incomingData`'s own keys into the write, when it is a map. Contributes nothing when it isn't. */
fun incomingUpdateData() = Update.UpdateDate.Incoming

/** Writes a literal map of key-value pairs, exploding each key into its own data id. */
fun inlineUpdateData(data: Map<String, AnySerializable?>) = Update.UpdateDate.Inline(data)

/** Writes a literal set of key-value pairs, exploding each key into its own data id. */
fun inlineUpdateData(vararg data: Pair<String, AnySerializable?>) = Update.UpdateDate.Inline(data.toMap())

/** Writes [value] as-is under the single, exact key [dataId] — no key explosion, so this is required whenever the value is itself a map/record. */
fun explicitUpdateData(dataId: String, value: AnySerializable?) =
    Update.UpdateDate.Explicit(dataId = dataId, value = Update.UpdateDate.Explicit.ExplicitValue.Inline(value))

/** Writes `incomingData` as-is under the single, exact key [dataId] — no key explosion, so this is required whenever `incomingData` is itself a map/record. */
fun explicitIncomingUpdateData(dataId: String) =
    Update.UpdateDate.Explicit(dataId = dataId, value = Update.UpdateDate.Explicit.ExplicitValue.Incoming)

/** Writes `null` under the single, exact key [dataId] — clears the entry on in-memory data holders; skipped on databases. */
fun explicitNullUpdateData(dataId: String) = explicitUpdateData(dataId, null)