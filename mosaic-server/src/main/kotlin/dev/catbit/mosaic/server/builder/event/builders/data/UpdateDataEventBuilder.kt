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

fun incomingUpdateData() = Update.UpdateDate.Incoming
fun inlineUpdateData(data: Map<String, AnySerializable?>) = Update.UpdateDate.Inline(data)
fun inlineUpdateData(vararg data: Pair<String, AnySerializable?>) = Update.UpdateDate.Inline(data.toMap())

fun explicitUpdateData(dataId: String, value: AnySerializable?) =
    Update.UpdateDate.Explicit(dataId = dataId, value = Update.UpdateDate.Explicit.ExplicitValue.Inline(value))

fun explicitIncomingUpdateData(dataId: String) =
    Update.UpdateDate.Explicit(dataId = dataId, value = Update.UpdateDate.Explicit.ExplicitValue.Incoming)

fun explicitNullUpdateData(dataId: String) = explicitUpdateData(dataId, null)