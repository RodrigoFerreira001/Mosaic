package dev.catbit.mosaic.server.builder.event

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.event.builders.data.DeleteDataDeletionBuilderScope
import dev.catbit.mosaic.server.builder.event.builders.data.GetDataReadingBuilderScope
import dev.catbit.mosaic.server.builder.event.builders.data.UpdateDataUpdateBuilderScope
import dev.catbit.mosaic.server.builder.event.builders.tiles.UpdateTilesUpdateBuilderScope

abstract class EventSchemaBuilder<out T : EventSchema> : GenericBuilder<T>() {
    operator fun GetDataReadingBuilderScope.Companion.invoke() = GetDataReadingBuilderScope(compositionLocals)
    operator fun DeleteDataDeletionBuilderScope.Companion.invoke() = DeleteDataDeletionBuilderScope(compositionLocals)
    operator fun UpdateDataUpdateBuilderScope.Companion.invoke() = UpdateDataUpdateBuilderScope(compositionLocals)
    operator fun UpdateTilesUpdateBuilderScope.Companion.invoke() = UpdateTilesUpdateBuilderScope(compositionLocals)
}