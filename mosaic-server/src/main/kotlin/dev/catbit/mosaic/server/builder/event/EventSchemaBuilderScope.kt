package dev.catbit.mosaic.server.builder.event

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider

class EventSchemaBuilderScope private constructor(): GenericBuilderScope<EventSchema, EventSchemaBuilder<*>>() {

    companion object {
        internal operator fun invoke(
            compositionLocals: Map<CompositionLocal<*>, ValueProvider<*>>
        ) = EventSchemaBuilderScope().apply { pushLocals(compositionLocals) }
    }
}