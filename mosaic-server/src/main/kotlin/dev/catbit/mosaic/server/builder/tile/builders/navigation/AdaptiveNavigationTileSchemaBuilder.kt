package dev.catbit.mosaic.server.builder.tile.builders.navigation

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.AdaptiveNavigationTileSchema
import dev.catbit.mosaic.core.extensions.randomUuid
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.composition_local.CompositionLocal
import dev.catbit.mosaic.server.builder.composition_local.ValueProvider
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.GetScreen
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class AdaptiveNavigationTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val navigatorId: String,
    private val startEntryId: String,
    private val selectedEntryId: String,
    private val header: (TileSchemaBuilderScope.() -> Unit)?,
    private val footer: (TileSchemaBuilderScope.() -> Unit)?,
    private val entries: AdaptiveNavigationEntryBuilderScope.() -> Unit,
) : TileSchemaBuilder<AdaptiveNavigationTileSchema>() {

    override fun build() = AdaptiveNavigationTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        navigatorId = navigatorId,
        startEntryId = startEntryId,
        selectedEntryId = selectedEntryId,
        header = header?.let { TileSchemaBuilderScope().apply(it).build().firstOrNull() },
        footer = footer?.let { TileSchemaBuilderScope().apply(it).build().firstOrNull() },
        entries = AdaptiveNavigationEntryBuilderScope().apply(entries).build(),
    )
}

fun TileSchemaBuilderScope.AdaptiveNavigation(
    id: String = randomUuid(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    navigatorId: String,
    startEntryId: String,
    selectedEntryId: String = startEntryId,
    header: (TileSchemaBuilderScope.() -> Unit)? = null,
    footer: (TileSchemaBuilderScope.() -> Unit)? = null,
    entries: AdaptiveNavigationEntryBuilderScope.() -> Unit,
) {
    addBuilder(
        AdaptiveNavigationTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            navigatorId = navigatorId,
            startEntryId = startEntryId,
            selectedEntryId = selectedEntryId,
            header = header,
            footer = footer,
            entries = entries,
        )
    )
}

class AdaptiveNavigationEntryBuilder(
    private val id: String,
    private val icon: IconSchema,
    private val label: String?,
    private val initialTiles: TileSchemaBuilderScope.() -> Unit,
    private val initialEvents: EventSchemaBuilderScope.() -> Unit,
    private val failureTiles: TileSchemaBuilderScope.() -> Unit,
    private val failureEvents: EventSchemaBuilderScope.() -> Unit,
) : GenericBuilder<AdaptiveNavigationTileSchema.NavigationEntry>() {

    override fun build() = AdaptiveNavigationTileSchema.NavigationEntry(
        id = id,
        icon = icon,
        label = label,
        initialTiles = TileSchemaBuilderScope().apply(initialTiles).build(),
        initialEvents = EventSchemaBuilderScope().apply(initialEvents).build(),
        failureTiles = TileSchemaBuilderScope().apply(failureTiles).build(),
        failureEvents = EventSchemaBuilderScope().apply(failureEvents).build(),
    )
}

class AdaptiveNavigationEntryBuilderScope private constructor() :
    GenericBuilderScope<AdaptiveNavigationTileSchema.NavigationEntry, AdaptiveNavigationEntryBuilder>() {

    companion object {
        internal operator fun invoke(
            compositionLocals: Map<CompositionLocal<*>, ValueProvider<*>>
        ) = AdaptiveNavigationEntryBuilderScope().apply { pushLocals(compositionLocals) }
    }

    fun entry(
        id: String,
        icon: IconSchema,
        label: String? = null,
        initialTiles: TileSchemaBuilderScope.() -> Unit = {},
        initialEvents: EventSchemaBuilderScope.() -> Unit = {
            GetScreen(
                trigger = EventTriggers.onDisplay(),
                events = {
                    ChangeScreenState(
                        trigger = EventTriggers.onSuccess(),
                        state = successState()
                    )
                }
            )
        },
        failureTiles: TileSchemaBuilderScope.() -> Unit = {},
        failureEvents: EventSchemaBuilderScope.() -> Unit = {},
    ) {
        addBuilder(
            AdaptiveNavigationEntryBuilder(
                id = id,
                icon = icon,
                label = label,
                initialTiles = initialTiles,
                initialEvents = initialEvents,
                failureTiles = failureTiles,
                failureEvents = failureEvents,
            )
        )
    }
}
