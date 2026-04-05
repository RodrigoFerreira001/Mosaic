package dev.catbit.mosaic.server.builder.tile.builders.menu

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class MenuTileSchemaBuilder(
    private val id: String,
    private val tiles: TileSchemaBuilderScope.() -> Unit,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val items: MenuItemSchemaBuilderScope.() -> Unit,
    private val expanded: Boolean
) : TileSchemaBuilder<MenuTileSchema>() {

    override fun build() = MenuTileSchema(
        id = id,
        tiles = TileSchemaBuilderScope().apply(tiles).build(),
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        items = MenuItemSchemaBuilderScope().apply(items).build(),
        expanded = expanded
    )
}

fun TileSchemaBuilderScope.Menu(
    id: String = randomUuid(),
    tiles: TileSchemaBuilderScope.() -> Unit,
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    items: MenuItemSchemaBuilderScope.() -> Unit,
    expanded: Boolean = false
) {
    addBuilder(
        MenuTileSchemaBuilder(
            id = id,
            tiles = tiles,
            events = events,
            style = style,
            visibility = visibility,
            items = items,
            expanded = expanded
        )
    )
}

class MenuItemSchemaBuilder(
    private val id: String,
    private val label: String,
    private val leadingIcon: IconSchema? = null,
    private val trailingIcon: IconSchema? = null
) : GenericBuilder<MenuTileSchema.MenuItem>() {

    override fun build() = MenuTileSchema.MenuItem(
        id = id,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}

class MenuItemSchemaBuilderScope private constructor(): GenericBuilderScope<MenuTileSchema.MenuItem, MenuItemSchemaBuilder>() {

    companion object {
        internal operator fun invoke(
            compositionLocals: Map<CompositionLocal<*>, ValueProvider<*>>
        ) = MenuItemSchemaBuilderScope().apply { pushLocals(compositionLocals) }
    }

    fun addMenuItem(
        id: String,
        label: String,
        leadingIcon: IconSchema? = null,
        trailingIcon: IconSchema? = null
    ) {
        addBuilder(
            MenuItemSchemaBuilder(
                id = id,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon
            )
        )
    }
}
