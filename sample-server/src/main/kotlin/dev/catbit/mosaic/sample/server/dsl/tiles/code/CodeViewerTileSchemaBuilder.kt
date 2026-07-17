package dev.catbit.mosaic.sample.server.dsl.tiles.code

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class CodeViewerTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val code: String,
    private val language: CodeViewerTileSchema.Language,
    private val theme: CodeViewerTileSchema.Theme,
) : TileSchemaBuilder<CodeViewerTileSchema>() {

    override fun build() = CodeViewerTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        code = code,
        language = language,
        theme = theme,
    )
}

fun TileSchemaBuilderScope.CodeViewer(
    code: String,
    language: CodeViewerTileSchema.Language,
    theme: CodeViewerTileSchema.Theme,
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
) {
    addBuilder(
        CodeViewerTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            code = code,
            language = language,
            theme = theme,
        )
    )
}
