package dev.catbit.mosaic.server.builder.tile.builders.navigation

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationBarTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class NavigationBarTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val items: NavigationBarItemSchemaBuilderScope.() -> Unit,
    private val selectedItemId: String
) : TileSchemaBuilder<NavigationBarTileSchema>() {

    override fun build() = NavigationBarTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        items = NavigationBarItemSchemaBuilderScope().apply(items).build(),
        selectedItemId = selectedItemId
    )
}

fun TileSchemaBuilderScope.NavigationBar(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    selectedItemId: String,
    items: NavigationBarItemSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        NavigationBarTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            items = items,
            selectedItemId = selectedItemId
        )
    )
}

class NavigationBarItemSchemaBuilder(
    private val id: String,
    private val icon: IconSchema,
    private val label: String? = null
) : GenericBuilder<NavigationBarTileSchema.NavigationBarItem>() {

    override fun build() = NavigationBarTileSchema.NavigationBarItem(
        id = id,
        icon = icon,
        label = label
    )
}

class NavigationBarItemSchemaBuilderScope private constructor():
    GenericBuilderScope<NavigationBarTileSchema.NavigationBarItem, NavigationBarItemSchemaBuilder>() {

    companion object {
        internal operator fun invoke(
            compositionLocals: Map<CompositionLocal<*>, ValueProvider<*>>
        ) = NavigationBarItemSchemaBuilderScope().apply { pushLocals(compositionLocals) }
    }

    fun item(
        id: String,
        icon: IconSchema,
        label: String? = null
    ) {
        addBuilder(
            NavigationBarItemSchemaBuilder(
                id = id,
                icon = icon,
                label = label
            )
        )
    }
}
