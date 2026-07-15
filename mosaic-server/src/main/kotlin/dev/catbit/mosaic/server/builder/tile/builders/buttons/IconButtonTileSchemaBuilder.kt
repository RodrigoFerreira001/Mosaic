package dev.catbit.mosaic.server.builder.tile.builders.buttons

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.IconButtonTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class IconButtonTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val icon: IconSchema,
    private val buttonType: IconButtonTileSchema.Type,
    private val loading: Boolean,
    private val enabled: Boolean
) : TileSchemaBuilder<IconButtonTileSchema>() {

    override fun build() = IconButtonTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        icon = icon,
        buttonType = buttonType,
        loading = loading,
        enabled = enabled
    )
}

fun TileSchemaBuilderScope.IconButton(
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
    buttonType: IconButtonTileSchema.Type = IconButtonTileSchema.Type.DEFAULT,
    loading: Boolean = false,
    enabled: Boolean = true
) {
    addBuilder(
        IconButtonTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            icon = icon,
            buttonType = buttonType,
            loading = loading,
            enabled = enabled
        )
    )
}

fun defaultIconButton() = IconButtonTileSchema.Type.DEFAULT
fun filledIconButton() = IconButtonTileSchema.Type.FILLED
fun filledTonalIconButton() = IconButtonTileSchema.Type.FILLED_TONAL
fun outlinedIconButton() = IconButtonTileSchema.Type.OUTLINED