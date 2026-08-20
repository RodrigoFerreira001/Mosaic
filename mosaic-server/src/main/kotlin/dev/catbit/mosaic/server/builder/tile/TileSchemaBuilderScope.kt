package dev.catbit.mosaic.server.builder.tile

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.server.builder.GenericBuilderScope

/**
 * The DSL receiver behind every `tiles = { ... }`/trailing-lambda block of tiles — what `Screen{}`,
 * a container tile's own trailing lambda (`Column { ... }`), and any custom `TileSchemaBuilderScope`
 * extension function (a reusable tile composition) run against. Each nested tile call inside the
 * block builds its own `TileSchemaBuilder` and registers it via
 * [addBuilder][dev.catbit.mosaic.server.builder.GenericBuilderScope.addBuilder]; the enclosing call
 * collects the results via [build][dev.catbit.mosaic.server.builder.GenericBuilderScope.build].
 */
class TileSchemaBuilderScope : GenericBuilderScope<TileSchema, TileSchemaBuilder<*>>()