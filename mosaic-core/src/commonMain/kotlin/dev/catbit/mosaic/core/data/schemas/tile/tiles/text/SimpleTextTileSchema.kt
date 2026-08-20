package dev.catbit.mosaic.core.data.schemas.tile.tiles.text

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.text.AutoSizeSchema
import dev.catbit.mosaic.core.data.schemas.text.FontFamilySchema
import dev.catbit.mosaic.core.data.schemas.text.FontStyleSchema
import dev.catbit.mosaic.core.data.schemas.text.FontWeightSchema
import dev.catbit.mosaic.core.data.schemas.text.TextAlignSchema
import dev.catbit.mosaic.core.data.schemas.text.TextDecorationSchema
import dev.catbit.mosaic.core.data.schemas.text.TextOverflowSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import dev.catbit.mosaic.core.data.schemas.typography.TypographySchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList

/**
 * Renders a Compose `Text` showing [text].
 *
 * **Text style:** [typography] provides the base `TextStyle`, falling back to the ambient
 * `LocalTextStyle` when `null`. Every other field overrides one property on top of that base and
 * is ignored when `null`: [color], [autoSize], [fontSize] (sp), [fontStyle], [fontWeight],
 * [fontFamily], [letterSpacing] (sp), [textDecoration], [textAlign] and [lineHeight] (sp).
 *
 * **Layout:** [overflow] defaults to clipping, [softWrap] to `true`, [maxLines] to unbounded and
 * [minLines] to `1`.
 *
 * **Triggers dispatched:** none. The tile emits no trigger and is not clickable, so any `events`
 * declared on it are never fired — wrap it in a `Box` or `Card` when you need tap handling.
 *
 * **Notes:** the tile renders plain text only — no inline annotations, links or markdown. Wrap
 * it in a `SelectionContainer` to make the text selectable.
 */
@Serializable
@SerialName("Text")
@Immutable
data class SimpleTextTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: SerializableImmutableList<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("searchableTerms") override val searchableTerms: SerializableImmutableList<String>?,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("text") val text: String,
    @SerialName("color") val color: ColorSchema?,
    @SerialName("typography") val typography: TypographySchema?,
    @SerialName("autoSize") val autoSize: AutoSizeSchema?,
    @SerialName("fontSize") val fontSize: Float?,
    @SerialName("fontStyle") val fontStyle: FontStyleSchema?,
    @SerialName("fontWeight") val fontWeight: FontWeightSchema?,
    @SerialName("fontFamily") val fontFamily: FontFamilySchema?,
    @SerialName("letterSpacing") val letterSpacing: Float?,
    @SerialName("textDecoration") val textDecoration: TextDecorationSchema?,
    @SerialName("textAlign") val textAlign: TextAlignSchema?,
    @SerialName("lineHeight") val lineHeight: Float?,
    @SerialName("overflow") val overflow: TextOverflowSchema?,
    @SerialName("softWrap") val softWrap: Boolean?,
    @SerialName("maxLines") val maxLines: Int?,
    @SerialName("minLines") val minLines: Int?
) : TileSchema