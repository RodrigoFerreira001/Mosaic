package dev.catbit.mosaic.server.builder.tile.builders.search

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class SearchBarTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val query: String,
    private val placeholder: String?,
    private val leadingIcon: (TileSchemaBuilderScope.() -> Unit)?,
    private val trailingIcon: (TileSchemaBuilderScope.() -> Unit)?
) : TileSchemaBuilder<SearchBarTileSchema>() {

    override fun build() = SearchBarTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        query = query,
        placeholder = placeholder,
        leadingIcon = leadingIcon?.let { TileSchemaBuilderScope().apply(it).build() }?.firstOrNull(),
        trailingIcon = trailingIcon?.let { TileSchemaBuilderScope().apply(it).build() }?.firstOrNull()
    )
}

fun TileSchemaBuilderScope.SearchBar(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    query: String = "",
    placeholder: String? = null,
    leadingIcon: (TileSchemaBuilderScope.() -> Unit)? = null,
    trailingIcon: (TileSchemaBuilderScope.() -> Unit)? = null
) {
    addBuilder(
        SearchBarTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            query = query,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    )
}
