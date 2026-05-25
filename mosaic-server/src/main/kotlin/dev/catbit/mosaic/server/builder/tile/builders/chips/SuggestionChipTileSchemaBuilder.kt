package dev.catbit.mosaic.server.builder.tile.builders.chips

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.SuggestionChipTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class SuggestionChipTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val text: String,
    private val enabled: Boolean
) : TileSchemaBuilder<SuggestionChipTileSchema>() {

    override fun build() = SuggestionChipTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        text = text,
        enabled = enabled
    )
}

fun TileSchemaBuilderScope.SuggestionChip(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    text: String,
    enabled: Boolean
) {
    addBuilder(
        SuggestionChipTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            text = text,
            enabled = enabled
        )
    )
}
