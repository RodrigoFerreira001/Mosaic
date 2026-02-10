package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.GetDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.GetDataEventSchema.Reading
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class GetDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val readings: GetDataReadingBuilderScope.() -> Unit
) : EventSchemaBuilder<GetDataEventSchema> {

    override fun build() = GetDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        readings = GetDataReadingBuilderScope().apply(readings).build()
    )
}

fun EventSchemaBuilderScope.GetData(
    id: String = randomUuid(),
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
) : GenericBuilder<Reading> {

    override fun build() = Reading(
        dataSource = dataSource,
        accessMode = accessMode
    )
}

class GetDataReadingBuilderScope : GenericBuilderScope<Reading, GetDataReadingBuilder>() {
    fun addReading(
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