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
import dev.catbit.mosaic.server.builder.tile.visible

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

/**
 * Renders a Material 3 icon button. The visual variant is picked by [buttonType] (default,
 * filled, filled tonal or outlined). Shows [icon] with its color, size and style applied. When
 * [loading] is `true` the button shows a spinner instead of the icon, and is disabled for real
 * regardless of [enabled] so a slow action cannot be submitted twice. Dispatches `onClick` when
 * tapped while interactive.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onClick`).
 * @param style Layout/appearance modifiers (size, padding, background, etc). Defaults to wrapping its content.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param icon Icon rendered inside the button.
 * @param buttonType Visual variant of the button — [defaultIconButton], [filledIconButton], [filledTonalIconButton] or [outlinedIconButton]. Defaults to default.
 * @param loading Whether to show a loading spinner instead of the icon and force the button disabled. Defaults to false.
 * @param enabled Whether the button is interactive. Ignored (treated as disabled) while [loading] is true. Defaults to true.
 */
fun TileSchemaBuilderScope.IconButton(
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
    icon: IconSchema,
    buttonType: IconButtonTileSchema.Type = defaultIconButton(),
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

/** Default icon button variant — no background, standard content color. */
fun defaultIconButton() = IconButtonTileSchema.Type.DEFAULT

/** Filled icon button variant — solid background, highest emphasis. */
fun filledIconButton() = IconButtonTileSchema.Type.FILLED

/** Filled tonal icon button variant — softer filled background, medium emphasis. */
fun filledTonalIconButton() = IconButtonTileSchema.Type.FILLED_TONAL

/** Outlined icon button variant — transparent background with an outline border. */
fun outlinedIconButton() = IconButtonTileSchema.Type.OUTLINED