package dev.catbit.mosaic.server.builder.event

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.server.builder.GenericBuilder

/** Base class every event builder extends (`SendNetworkRequestEventSchemaBuilder`,
 * `UpdateDataEventSchemaBuilder`, etc.) — produces the concrete [EventSchema] instance [T] the DSL
 * call compiles down to. */
abstract class EventSchemaBuilder<out T : EventSchema> : GenericBuilder<T>()
