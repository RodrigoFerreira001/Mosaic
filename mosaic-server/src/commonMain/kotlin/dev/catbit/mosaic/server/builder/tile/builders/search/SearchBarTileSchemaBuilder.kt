package dev.catbit.mosaic.server.builder.tile.builders.search

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class SearchBarTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val query: String,
    private val placeholder: String?,
    private val leadingIcon: (TileSchemaBuilderScope.() -> Unit)?,
    private val trailingIcon: (TileSchemaBuilderScope.() -> Unit)?
) : TileSchemaBuilder<SearchBarTileSchema> {

    override fun build() = SearchBarTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilder().apply { StyleSchemaBuilderScope().apply(style) }.build(),
        visibility = visibility,
        query = query,
        placeholder = placeholder,
        leadingIcon = leadingIcon?.let { TileSchemaBuilderScope().apply(it).build() }?.firstOrNull(),
        trailingIcon = trailingIcon?.let { TileSchemaBuilderScope().apply(it).build() }?.firstOrNull()
    )
}

fun TileSchemaBuilderScope.SearchBar(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilder.StyleSchemaBuilderScope.() -> Unit = {},
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
