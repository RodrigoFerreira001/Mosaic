package dev.catbit.mosaic.server.builder.tile.builders.chips

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.AssistChipTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class AssistChipTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val text: String,
    private val leadingIcon: IconSchema?,
    private val trailingIcon: IconSchema?,
    private val enabled: Boolean,
    private val variant: AssistChipTileSchema.Variant,
) : TileSchemaBuilder<AssistChipTileSchema>() {

    override fun build() = AssistChipTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        text = text,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
        variant = variant,
    )
}

/**
 * Renders a Material 3 assist chip displaying [text] as its label, with optional [leadingIcon]
 * and [trailingIcon]. [variant] picks the visual style — default (outlined) or elevated. The
 * chip is stateless (no selected state) and both icons are purely decorative — tapping either
 * fires the same chip-level `onClick`. Dispatches `onClick` when tapped.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onClick`).
 * @param style Layout/appearance modifiers (size, padding, background, etc). Defaults to wrapping its content.
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param text Label displayed on the chip.
 * @param leadingIcon Optional icon rendered before the text. Defaults to none.
 * @param trailingIcon Optional icon rendered after the text. Defaults to none.
 * @param enabled Whether the chip is interactive. Defaults to true.
 * @param variant Visual style of the chip — [defaultAssistChip] (outlined) or [elevatedAssistChip]. Defaults to default.
 */
fun TileSchemaBuilderScope.AssistChip(
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
    text: String,
    leadingIcon: IconSchema? = null,
    trailingIcon: IconSchema? = null,
    enabled: Boolean = true,
    variant: AssistChipTileSchema.Variant = defaultAssistChip(),
) {
    addBuilder(
        AssistChipTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            text = text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            enabled = enabled,
            variant = variant,
        )
    )
}

/** Default assist chip variant — outlined. */
fun defaultAssistChip() = AssistChipTileSchema.Variant.DEFAULT

/** Elevated assist chip variant — shadowed surface-colored background. */
fun elevatedAssistChip() = AssistChipTileSchema.Variant.ELEVATED
