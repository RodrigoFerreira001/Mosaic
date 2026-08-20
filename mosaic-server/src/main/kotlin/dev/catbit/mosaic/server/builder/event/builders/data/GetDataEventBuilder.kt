package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.GetDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.GetDataEventSchema.Reading
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class GetDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val readings: GetDataReadingBuilderScope.() -> Unit
) : EventSchemaBuilder<GetDataEventSchema>() {

    override fun build() = GetDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        readings = GetDataReadingBuilderScope().apply(readings).build()
    )
}

/**
 * Reads one or more values from data sources and emits them downstream, on the IO dispatcher.
 * Each [readings] entry (built with `reading`) pairs a data source with an access mode; readings
 * are processed in order into a single accumulator, so a later reading overwrites an earlier one
 * on key collision. `Full` access mode, and `Batch` without `unwrapValuesToList`, produce a map
 * keyed by data id; `Batch` with `unwrapValuesToList` produces a list of values; with only
 * `Single` readings, one reading emits the bare value and several emit a list — a map result wins
 * over a list result when the two are mixed. Does not consume `incomingData`. Dispatches
 * `onStart` before any reading runs, `onSuccess` (carrying the assembled result) once every
 * reading resolves, and `onFailure` on the first problem — a `Single` id resolving to `null`, a
 * missing `Batch` id with `allowMissingData` false, or a database read failure.
 *
 * @param id Unique identifier of this event. Defaults to a random id.
 * @param trigger Trigger that fires this event, built via `EventTriggers`.
 * @param events Child events chained after this one, wired to its triggers (`onStart`, `onSuccess`, `onFailure`).
 * @param readings Data sources to read, declared with `reading`.
 */
fun EventSchemaBuilderScope.GetData(
    id: String = randomId(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    readings: GetDataReadingBuilderScope.() -> Unit
) {
    addBuilder(
        GetDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            readings = readings
        )
    )
}

class GetDataReadingBuilder(
    private val dataSource: DataSourceSchema,
    private val accessMode: AccessModeSchema
) : GenericBuilder<Reading>() {

    override fun build() = Reading(
        dataSource = dataSource,
        accessMode = accessMode
    )
}

class GetDataReadingBuilderScope : GenericBuilderScope<Reading, GetDataReadingBuilder>() {

    /**
     * Declares one value to read inside a `GetData` event.
     *
     * @param dataSource Where the value comes from, built with the `data` package helpers (e.g. `screenPlainData()`, `tile(...)`).
     * @param accessMode How much of the source to read, built with [dev.catbit.mosaic.server.builder.data.singleAccessMode], [dev.catbit.mosaic.server.builder.data.batchAccessMode] or [dev.catbit.mosaic.server.builder.data.fullAccessMode].
     */
    fun reading(
        dataSource: DataSourceSchema,
        accessMode: AccessModeSchema
    ) {
        addBuilder(
            GetDataReadingBuilder(
                dataSource = dataSource,
                accessMode = accessMode,
            )
        )
    }
}