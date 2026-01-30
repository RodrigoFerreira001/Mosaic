package dev.catbit.mosaic.client.ui.sdui.foundation.definitions

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunner
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilder
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import kotlin.reflect.KClass

interface EventDefinition <Schema: EventSchema> {
    val eventSchemaClass: KClass<Schema>
    val eventRunner: EventRunner<Schema>
    val eventHolderBuilder: EventHolderBuilder<Schema, out EventHolder<Schema>>
}