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
import dev.catbit.mosaic.server.builder.tile.visible

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

/**
 * Renders plain text showing [text]. [typography] provides the base text style, falling back to
 * the ambient default when `null`; every other styling field overrides one property on top of
 * that base and is ignored when `null`: [color], [autoSize], [fontSize] (sp), [fontStyle],
 * [fontWeight], [fontFamily], [letterSpacing] (sp), [textDecoration], [textAlign] and
 * [lineHeight] (sp). [overflow] defaults to clipping, [softWrap] to `true`, [maxLines] to
 * unbounded and [minLines] to 1. Dispatches no triggers and is not clickable — wrap it in a `Box`
 * or `Card` for tap handling. Renders plain text only — no inline annotations, links or markdown;
 * wrap it in a `SelectionContainer` to make the text selectable.
 *
 * @param text Text content displayed.
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile. Never fired, since the tile dispatches no triggers.
 * @param style Layout/appearance modifiers (size, padding, background, etc). Defaults to wrapping its content.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param color Text color override. Defaults to none (uses the base style's color).
 * @param typography Base text style (size, weight, line height, etc as a set). Defaults to none (uses the ambient default).
 * @param autoSize Automatic font-size scaling to fit the available space. Defaults to none (disabled).
 * @param fontSize Font size override, in sp. Defaults to none (uses the base style's size).
 * @param fontStyle Font style override (e.g. italic). Defaults to none (uses the base style's).
 * @param fontWeight Font weight override. Defaults to none (uses the base style's).
 * @param fontFamily Font family override. Defaults to none (uses the base style's).
 * @param letterSpacing Letter spacing override, in sp. Defaults to none (uses the base style's).
 * @param textDecoration Text decoration override (e.g. underline). Defaults to none (uses the base style's).
 * @param textAlign Text alignment override. Defaults to none (uses the base style's).
 * @param lineHeight Line height override, in sp. Defaults to none (uses the base style's).
 * @param overflow How overflowing text is handled. Defaults to clipping.
 * @param softWrap Whether the text wraps at soft line breaks. Defaults to true.
 * @param maxLines Maximum number of lines shown. Defaults to unbounded.
 * @param minLines Minimum number of lines reserved. Defaults to 1.
 */
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
    visibility: TileSchema.Visibility = visible(),
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
