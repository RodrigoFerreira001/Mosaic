package dev.catbit.mosaic.server.builder.tile.builders.progress

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.LinearProgressIndicatorTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class LinearProgressIndicatorTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val progress: Float?
) : TileSchemaBuilder<LinearProgressIndicatorTileSchema>() {

    override fun build() = LinearProgressIndicatorTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        progress = progress
    )
}

fun TileSchemaBuilderScope.LinearProgressIndicator(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    progress: Float? = null
) {
    addBuilder(
        LinearProgressIndicatorTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            progress = progress
        )
    )
}
