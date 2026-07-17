package dev.catbit.mosaic.sample.server.dsl.tiles.navigation

import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.icon.IconSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.sample.core.schemas.tiles.navigation.AdaptiveNavigationTileSchema
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.GetScreen
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.navigation.NestedNavigationGraphEntryBuilderScope
import kotlinx.collections.immutable.toImmutableList

internal class AdaptiveNavigationTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    private val navigatorId: String,
    private val startEntryId: String,
    private val selectedEntryId: String,
    private val primaryAction: (TileSchemaBuilderScope.() -> Unit)?,
    private val topBar: AdaptiveNavigationTopBarSchemaBuilder.() -> Unit,
    private val entries: AdaptiveNavigationEntryBuilderScope.() -> Unit,
    private val hiddenEntries: (NestedNavigationGraphEntryBuilderScope.() -> Unit)? = null,
    private val defaultTransition: ContentTransitionSchema? = null,
    private val defaultPopTransition: ContentTransitionSchema? = null,
    private val defaultPredictivePopTransition: ContentTransitionSchema? = null,
) : TileSchemaBuilder<AdaptiveNavigationTileSchema>() {

    override fun build() = AdaptiveNavigationTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        navigatorId = navigatorId,
        startEntryId = startEntryId,
        selectedEntryId = selectedEntryId,
        primaryAction = primaryAction?.let {
            TileSchemaBuilderScope().apply(it).build().firstOrNull()
        },
        topBar = AdaptiveNavigationTopBarSchemaBuilder().apply(topBar).build(),
        entries = AdaptiveNavigationEntryBuilderScope().apply(entries).build(),
        hiddenEntries = hiddenEntries?.let {
            NestedNavigationGraphEntryBuilderScope().apply(it).build()
        },
        defaultTransition = defaultTransition,
        defaultPopTransition = defaultPopTransition,
        defaultPredictivePopTransition = defaultPredictivePopTransition,
    )
}

fun TileSchemaBuilderScope.AdaptiveNavigation(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    navigatorId: String,
    startEntryId: String,
    selectedEntryId: String = startEntryId,
    primaryAction: (TileSchemaBuilderScope.() -> Unit)? = null,
    topBar: AdaptiveNavigationTopBarSchemaBuilder.() -> Unit,
    hiddenEntries: (NestedNavigationGraphEntryBuilderScope.() -> Unit)? = null,
    defaultTransition: ContentTransitionSchema? = null,
    defaultPopTransition: ContentTransitionSchema? = null,
    defaultPredictivePopTransition: ContentTransitionSchema? = null,
    entries: AdaptiveNavigationEntryBuilderScope.() -> Unit,
) {
    addBuilder(
        AdaptiveNavigationTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            searchableTerms = searchableTerms,
            visibility = visibility,
            navigatorId = navigatorId,
            startEntryId = startEntryId,
            selectedEntryId = selectedEntryId,
            primaryAction = primaryAction,
            topBar = topBar,
            entries = entries,
            hiddenEntries = hiddenEntries,
            defaultTransition = defaultTransition,
            defaultPopTransition = defaultPopTransition,
            defaultPredictivePopTransition = defaultPredictivePopTransition,
        )
    )
}

class AdaptiveNavigationTopBarSchemaBuilder :
    GenericBuilder<AdaptiveNavigationTileSchema.TopBarSchema>() {
    var barStyle: AdaptiveNavigationTileSchema.TopBarSchema.BarStyle =
        AdaptiveNavigationTileSchema.TopBarSchema.BarStyle.DEFAULT
    private var backgroundColor: ColorSchema? = null
    private var title: (TileSchemaBuilderScope.() -> Unit)? = null
    private var subtitle: (TileSchemaBuilderScope.() -> Unit)? = null
    private var actions: (TileSchemaBuilderScope.() -> Unit)? = null

    fun title(block: TileSchemaBuilderScope.() -> Unit) {
        title = block
    }

    fun subtitle(block: TileSchemaBuilderScope.() -> Unit) {
        subtitle = block
    }

    fun actions(block: TileSchemaBuilderScope.() -> Unit) {
        actions = block
    }

    fun backgroundColor(color: ColorSchema) {
        backgroundColor = color
    }

    override fun build() = AdaptiveNavigationTileSchema.TopBarSchema(
        title = checkNotNull(title?.let {
            TileSchemaBuilderScope().apply(it).build().firstOrNull()
        }) {
            "AdaptiveNavigation topBar requires a title tile"
        },
        actions = actions?.let { actions ->
            TileSchemaBuilderScope().apply(actions).build().takeIf { it.isNotEmpty() }
        },
        subtitle = subtitle?.let { subtitle ->
            TileSchemaBuilderScope().apply(subtitle).build().takeIf { it.isNotEmpty() }
        },
        barStyle = barStyle,
        backgroundColor = backgroundColor,
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
    private val transition: ContentTransitionSchema? = null,
    private val popTransition: ContentTransitionSchema? = null,
    private val predictivePopTransition: ContentTransitionSchema? = null,
) : GenericBuilder<AdaptiveNavigationTileSchema.NavigationEntry>() {

    override fun build() = AdaptiveNavigationTileSchema.NavigationEntry(
        id = id,
        icon = icon,
        label = label,
        initialTiles = TileSchemaBuilderScope().apply(initialTiles).build(),
        initialEvents = EventSchemaBuilderScope().apply(initialEvents).build(),
        failureTiles = TileSchemaBuilderScope().apply(failureTiles).build(),
        failureEvents = EventSchemaBuilderScope().apply(failureEvents).build(),
        transition = transition,
        popTransition = popTransition,
        predictivePopTransition = predictivePopTransition,
    )
}

class AdaptiveNavigationEntryBuilderScope :
    GenericBuilderScope<AdaptiveNavigationTileSchema.NavigationEntry, AdaptiveNavigationEntryBuilder>() {

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
        transition: ContentTransitionSchema? = null,
        popTransition: ContentTransitionSchema? = null,
        predictivePopTransition: ContentTransitionSchema? = null,
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
                transition = transition,
                popTransition = popTransition,
                predictivePopTransition = predictivePopTransition,
            )
        )
    }
}
