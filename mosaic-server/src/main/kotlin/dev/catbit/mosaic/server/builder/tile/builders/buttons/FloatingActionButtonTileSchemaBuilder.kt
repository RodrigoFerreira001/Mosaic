package dev.catbit.mosaic.server.builder.tile.builders.buttons

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.FloatingActionButtonTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class FloatingActionButtonTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val icon: IconSchema,
    private val size: FloatingActionButtonTileSchema.Size,
    private val loading: Boolean,
    private val enabled: Boolean
) : TileSchemaBuilder<FloatingActionButtonTileSchema>() {

    override fun build() = FloatingActionButtonTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        icon = when(size) {
            FloatingActionButtonTileSchema.Size.DEFAULT -> icon.copy(size = 24)
            FloatingActionButtonTileSchema.Size.MEDIUM -> icon.copy(size = 28)
            FloatingActionButtonTileSchema.Size.LARGE -> icon.copy(size = 36)
        },
        size = size,
        loading = loading,
        enabled = enabled
    )
}

fun TileSchemaBuilderScope.FloatingActionButton(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {
        size(
            width = wrapHorizontally(),
            height = wrapVertically()
        )
    },
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    searchableTerms: List<String>? = null,
    icon: IconSchema,
    size: FloatingActionButtonTileSchema.Size = FloatingActionButtonTileSchema.Size.DEFAULT,
    loading: Boolean = false,
    enabled: Boolean = true
) {
    addBuilder(
        FloatingActionButtonTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            icon = icon,
            size = size,
            loading = loading,
            enabled = enabled
        )
    )
}

fun defaultFloatingActionButon() = FloatingActionButtonTileSchema.Size.DEFAULT
fun mediumFloatingActionButon() = FloatingActionButtonTileSchema.Size.MEDIUM
fun largeFloatingActionButon() = FloatingActionButtonTileSchema.Size.LARGE