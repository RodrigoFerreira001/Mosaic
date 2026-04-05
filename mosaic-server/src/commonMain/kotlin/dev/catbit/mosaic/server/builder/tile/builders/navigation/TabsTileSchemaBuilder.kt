package dev.catbit.mosaic.server.builder.tile.builders.navigation

import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class TabsTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val selectedTabId: String,
    private val tabItems: TabItemSchemaBuilderScope.() -> Unit,
    private val tabType: TabsTileSchema.Type,
    private val scrollable: Boolean
) : TileSchemaBuilder<TabsTileSchema>() {

    override fun build() = TabsTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        selectedTabId = selectedTabId,
        tabItems = TabItemSchemaBuilderScope().apply(tabItems).build(),
        tabType = tabType,
        scrollable = scrollable
    )
}

fun TileSchemaBuilderScope.Tabs(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    selectedTabId: String,
    tabType: TabsTileSchema.Type = TabsTileSchema.Type.PRIMARY,
    scrollable: Boolean = false,
    tabItems: TabItemSchemaBuilderScope.() -> Unit
) {
    addBuilder(
        TabsTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            selectedTabId = selectedTabId,
            tabItems = tabItems,
            tabType = tabType,
            scrollable = scrollable
        )
    )
}

class TabItemSchemaBuilder(
    private val id: String,
    private val label: String? = null,
    private val icon: IconSchema? = null,
    private val badgeText: String? = null
) : GenericBuilder<TabsTileSchema.TabItem>() {

    override fun build() = TabsTileSchema.TabItem(
        id = id,
        label = label,
        icon = icon,
        badgeText = badgeText
    )
}

class TabItemSchemaBuilderScope private constructor() :
    GenericBuilderScope<TabsTileSchema.TabItem, TabItemSchemaBuilder>() {

    companion object {
        internal operator fun invoke(
            compositionLocals: Map<CompositionLocal<*>, ValueProvider<*>>
        ) = TabItemSchemaBuilderScope().apply { pushLocals(compositionLocals) }
    }

    fun addTab(
        id: String,
        label: String? = null,
        icon: IconSchema? = null,
        badgeText: String? = null
    ) {
        addBuilder(
            TabItemSchemaBuilder(
                id = id,
                label = label,
                icon = icon,
                badgeText = badgeText
            )
        )
    }
}
