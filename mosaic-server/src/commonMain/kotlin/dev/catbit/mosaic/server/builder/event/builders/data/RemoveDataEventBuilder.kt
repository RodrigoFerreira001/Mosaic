package dev.catbit.mosaic.server.builder.event.builders.data

import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema.Deletion
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilder
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope

internal class RemoveDataEventBuilder(
    private val id: String,
    private val trigger: EventTrigger,
    private val events: EventSchemaBuilderScope.() -> Unit = {},
    private val deletions: DeleteDataDeletionBuilderScope.() -> Unit
) : EventSchemaBuilder<RemoveDataEventSchema>() {

    override fun build() = RemoveDataEventSchema(
        id = id,
        trigger = trigger,
        events = EventSchemaBuilderScope().apply(events).build(),
        deletions = DeleteDataDeletionBuilderScope().apply(deletions).build()
    )
}

fun EventSchemaBuilderScope.RemoveData(
    id: String = randomUuid(),
    trigger: EventTrigger,
    events: EventSchemaBuilderScope.() -> Unit = {},
    deletions: DeleteDataDeletionBuilderScope.() -> Unit
) {
    addBuilder(
        RemoveDataEventBuilder(
            id = id,
            trigger = trigger,
            events = events,
            deletions = deletions
        )
    )
}

class DeleteDataDeletionBuilder(
    private val dataSource: DataSourceSchema,
    private val accessMode: AccessModeSchema
) : GenericBuilder<Deletion>() {

    override fun build() = Deletion(
        dataSource = dataSource,
        accessMode = accessMode
    )
}

class DeleteDataDeletionBuilderScope private constructor(): GenericBuilderScope<Deletion, DeleteDataDeletionBuilder>() {

    companion object {
        internal operator fun invoke(
            compositionLocals: Map<CompositionLocal<*>, ValueProvider<*>>
        ) = DeleteDataDeletionBuilderScope().apply { pushLocals(compositionLocals) }
    }

    fun addDeletion(
        dataSource: DataSourceSchema,
        accessMode: AccessModeSchema
    ) {
        addBuilder(
            DeleteDataDeletionBuilder(
                dataSource = dataSource,
                accessMode = accessMode,
            )
        )
    }
}