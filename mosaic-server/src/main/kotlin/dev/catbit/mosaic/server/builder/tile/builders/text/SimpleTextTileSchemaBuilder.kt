package dev.catbit.mosaic.server.builder.tile.builders.text

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.text.AutoSizeSchema
import dev.catbit.mosaic.core.data.schemas.text.FontFamilySchema
import dev.catbit.mosaic.core.data.schemas.text.FontStyleSchema
import dev.catbit.mosaic.core.data.schemas.text.FontWeightSchema
import dev.catbit.mosaic.core.data.schemas.text.TextAlignSchema
import dev.catbit.mosaic.core.data.schemas.text.TextDecorationSchema
import dev.catbit.mosaic.core.data.schemas.text.TextOverflowSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.SimpleTextTileSchema
import dev.catbit.mosaic.core.data.schemas.typography.TypographySchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class SimpleTextTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val text: String,
    private val color: ColorSchema?,
    private val typography: TypographySchema?,
    private val autoSize: AutoSizeSchema?,
    private val fontSize: Float?,
    private val fontStyle: FontStyleSchema?,
    private val fontWeight: FontWeightSchema?,
    private val fontFamily: FontFamilySchema?,
    private val letterSpacing: Float?,
    private val textDecoration: TextDecorationSchema?,
    private val textAlign: TextAlignSchema?,
    private val lineHeight: Float?,
    private val overflow: TextOverflowSchema?,
    private val softWrap: Boolean?,
    private val maxLines: Int?,
    private val minLines: Int?
) : TileSchemaBuilder<SimpleTextTileSchema>() {

    override fun build() = SimpleTextTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        text = text,
        color = color,
        typography = typography,
        autoSize = autoSize,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines
    )
}

fun TileSchemaBuilderScope.SimpleText(
    text: String,
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
    color: ColorSchema? = null,
    typography: TypographySchema? = null,
    autoSize: AutoSizeSchema? = null,
    fontSize: Float? = null,
    fontStyle: FontStyleSchema? = null,
    fontWeight: FontWeightSchema? = null,
    fontFamily: FontFamilySchema? = null,
    letterSpacing: Float? = null,
    textDecoration: TextDecorationSchema? = null,
    textAlign: TextAlignSchema? = null,
    lineHeight: Float? = null,
    overflow: TextOverflowSchema? = null,
    softWrap: Boolean? = null,
    maxLines: Int? = null,
    minLines: Int? = null
) {
    addBuilder(
        SimpleTextTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            text = text,
            color = color,
            typography = typography,
            autoSize = autoSize,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines
        )
    )
}
