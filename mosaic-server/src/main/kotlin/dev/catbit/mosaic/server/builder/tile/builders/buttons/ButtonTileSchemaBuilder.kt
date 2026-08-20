package dev.catbit.mosaic.server.builder.tile.builders.buttons

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class ButtonTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val text: String,
    private val icon: IconSchema?,
    private val buttonType: ButtonTileSchema.Type,
    private val shape: ButtonTileSchema.Shape,
    private val loading: Boolean,
    private val enabled: Boolean,
    private val iconPosition: ButtonTileSchema.IconPosition
) : TileSchemaBuilder<ButtonTileSchema>() {

    override fun build() = ButtonTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        text = text,
        icon = icon,
        buttonType = buttonType,
        shape = shape,
        loading = loading,
        enabled = enabled,
        iconPosition = iconPosition
    )
}

/**
 * Renders a Material 3 button. The visual variant is picked by [buttonType] (filled, elevated,
 * filled tonal, outlined or text) and its corner style by [shape] (square or rounded). Shows
 * [text] together with an optional [icon] positioned by [iconPosition]. When [loading] is `true`
 * the button shows a spinner instead of its content, and is disabled for real regardless of
 * [enabled] so a slow action cannot be submitted twice. Dispatches `onClick` when tapped while
 * interactive.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onClick`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param text Label displayed on the button.
 * @param icon Optional icon rendered alongside the text. Defaults to none.
 * @param buttonType Visual variant of the button — [filledButton], [elevatedButton], [filledTonalButton], [outlinedButton] or [textButton]. Defaults to filled.
 * @param shape Corner style of the button — [squareButton] or [roundedButton]. Defaults to rounded.
 * @param loading Whether to show a loading spinner instead of the content and force the button disabled. Defaults to false.
 * @param enabled Whether the button is interactive. Ignored (treated as disabled) while [loading] is true. Defaults to true.
 * @param iconPosition Where the icon is placed relative to the text — [iconAtStart] or [iconAtEnd]. Defaults to start.
 */
fun TileSchemaBuilderScope.Button(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
    text: String,
    icon: IconSchema? = null,
    buttonType: ButtonTileSchema.Type = filledButton(),
    shape: ButtonTileSchema.Shape = roundedButton(),
    loading: Boolean = false,
    enabled: Boolean = true,
    iconPosition: ButtonTileSchema.IconPosition = iconAtStart()
) {
    addBuilder(
        ButtonTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            text = text,
            icon = icon,
            buttonType = buttonType,
            shape = shape,
            loading = loading,
            enabled = enabled,
            iconPosition = iconPosition
        )
    )
}

/** Filled button variant — solid background, highest emphasis. */
fun filledButton() = ButtonTileSchema.Type.FILLED

/** Elevated button variant — shadowed surface-colored background, for use on top of other filled surfaces. */
fun elevatedButton() = ButtonTileSchema.Type.ELEVATED

/** Filled tonal button variant — softer filled background, medium emphasis. */
fun filledTonalButton() = ButtonTileSchema.Type.FILLED_TONAL

/** Outlined button variant — transparent background with an outline border, medium emphasis. */
fun outlinedButton() = ButtonTileSchema.Type.OUTLINED

/** Text button variant — no background or border, lowest emphasis. */
fun textButton() = ButtonTileSchema.Type.TEXT


/** Square button shape — mapped to the theme's medium corner radius. */
fun squareButton() = ButtonTileSchema.Shape.SQUARE

/** Rounded button shape — fully rounded (pill/circle) corners. */
fun roundedButton() = ButtonTileSchema.Shape.ROUNDED


/** Places the icon before the text. */
fun iconAtStart() = ButtonTileSchema.IconPosition.START

/** Places the icon after the text. */
fun iconAtEnd() = ButtonTileSchema.IconPosition.END