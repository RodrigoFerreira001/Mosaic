package dev.catbit.mosaic.server.builder.tile.builders.navigation

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationRailTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class NavigationRailTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val items: NavigationRailItemSchemaBuilderScope.() -> Unit,
    private val selectedItemId: String,
    private val header: (TileSchemaBuilderScope.() -> Unit)? = null,
    private val footer: (TileSchemaBuilderScope.() -> Unit)? = null
) : TileSchemaBuilder<NavigationRailTileSchema>() {

    override fun build() = NavigationRailTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        items = NavigationRailItemSchemaBuilderScope().apply(items).build(),
        selectedItemId = selectedItemId,
        header = header?.let { TileSchemaBuilderScope().apply(it).build().firstOrNull() },
        footer = footer?.let { TileSchemaBuilderScope().apply(it).build().firstOrNull() }
    )
}

fun TileSchemaBuilderScope.NavigationRail(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    selectedItemId: String,
    header: (TileSchemaBuilderScope.() -> Unit)? = null,
    footer: (TileSchemaBuilderScope.() -> Unit)? = null,
    items: NavigationRailItemSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        NavigationRailTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            items = items,
            selectedItemId = selectedItemId,
            header = header,
            footer = footer
        )
    )
}

class NavigationRailItemSchemaBuilder(
    private val id: String,
    private val icon: IconSchema,
    private val label: String? = null
) : GenericBuilder<NavigationRailTileSchema.NavigationRailItem>() {

    override fun build() = NavigationRailTileSchema.NavigationRailItem(
        id = id,
        icon = icon,
        label = label
    )
}

class NavigationRailItemSchemaBuilderScope private constructor(): GenericBuilderScope<NavigationRailTileSchema.NavigationRailItem, NavigationRailItemSchemaBuilder>() {

    companion object {
        internal operator fun invoke(
            compositionLocals: Map<CompositionLocal<*>, ValueProvider<*>>
        ) = NavigationRailItemSchemaBuilderScope().apply { pushLocals(compositionLocals) }
    }

    fun addItem(
        id: String,
        icon: IconSchema,
        label: String? = null
    ) {
        addBuilder(
            NavigationRailItemSchemaBuilder(
                id = id,
                icon = icon,
                label = label
            )
        )
    }
}
