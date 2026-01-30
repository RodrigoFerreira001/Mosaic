package dev.catbit.mosaic.server.builder.event

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.server.builder.GenericBuilderScope

class EventSchemaBuilderScope : GenericBuilderScope<EventSchema, EventSchemaBuilder<*>>()