package dev.catbit.mosaic.server.builder.event

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.server.builder.GenericBuilder

abstract class EventSchemaBuilder<out T : EventSchema> : GenericBuilder<T>()
