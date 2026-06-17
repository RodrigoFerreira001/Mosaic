package dev.catbit.mosaic.server.builder.tile.builders.chips

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.FilterChipTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class FilterChipTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val text: String,
    private val selected: Boolean,
    private val leadingIcon: IconSchema?,
    private val trailingIcon: IconSchema?,
    private val enabled: Boolean,
    private val variant: FilterChipTileSchema.Variant,
) : TileSchemaBuilder<FilterChipTileSchema>() {

    override fun build() = FilterChipTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        text = text,
        selected = selected,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
        variant = variant,
    )
}

fun TileSchemaBuilderScope.FilterChip(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {
        size(
            width = wrapHorizontally(),
            height = wrapVertically()
        )
    },
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    text: String,
    selected: Boolean = false,
    leadingIcon: IconSchema? = null,
    trailingIcon: IconSchema? = null,
    enabled: Boolean = true,
    variant: FilterChipTileSchema.Variant = FilterChipTileSchema.Variant.DEFAULT,
) {
    addBuilder(
        FilterChipTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            text = text,
            selected = selected,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            enabled = enabled,
            variant = variant,
        )
    )
}

fun defaultFilterChip() = FilterChipTileSchema.Variant.DEFAULT
fun elevatedFilterChip() = FilterChipTileSchema.Variant.ELEVATED
