package dev.catbit.mosaic.server.builder.tile.builders.navigation

import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NestedNavigationGraphTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.GetScreen
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

internal class NestedNavigationGraphTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val visibility: TileSchema.Visibility,
    private val navigatorId: String,
    private val startEntryId: String,
    private val entries: NestedNavigationGraphEntryBuilderScope.() -> Unit,
    private val defaultTransition: ContentTransitionSchema? = null,
    private val defaultPopTransition: ContentTransitionSchema? = null,
    private val defaultPredictivePopTransition: ContentTransitionSchema? = null,
) : TileSchemaBuilder<NestedNavigationGraphTileSchema>() {

    override fun build() = NestedNavigationGraphTileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        visibility = visibility,
        navigatorId = navigatorId,
        entries = NestedNavigationGraphEntryBuilderScope().apply(entries).build(),
        startEntryId = startEntryId,
        defaultTransition = defaultTransition,
        defaultPopTransition = defaultPopTransition,
        defaultPredictivePopTransition = defaultPredictivePopTransition,
    )
}

fun TileSchemaBuilderScope.NestedNavigationGraph(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    navigatorId: String,
    startEntryId: String,
    defaultTransition: ContentTransitionSchema? = null,
    defaultPopTransition: ContentTransitionSchema? = null,
    defaultPredictivePopTransition: ContentTransitionSchema? = null,
    entries: NestedNavigationGraphEntryBuilderScope.() -> Unit
) {
    addBuilder(
        NestedNavigationGraphTileSchemaBuilder(
            id = id,
            events = events,
            style = style,
            visibility = visibility,
            navigatorId = navigatorId,
            entries = entries,
            startEntryId = startEntryId,
            defaultTransition = defaultTransition,
            defaultPopTransition = defaultPopTransition,
            defaultPredictivePopTransition = defaultPredictivePopTransition,
        )
    )
}

class NestedNavigationGraphEntryBuilder(
    private val screenId: String,
    private val initialTiles: TileSchemaBuilderScope.() -> Unit = {},
    private val initialEvents: EventSchemaBuilderScope.() -> Unit = {},
    private val failureTiles: TileSchemaBuilderScope.() -> Unit = {},
    private val failureEvents: EventSchemaBuilderScope.() -> Unit = {},
    private val transition: ContentTransitionSchema? = null,
    private val popTransition: ContentTransitionSchema? = null,
    private val predictivePopTransition: ContentTransitionSchema? = null,
) : GenericBuilder<NestedNavigationGraphTileSchema.Entry>() {

    override fun build() = NestedNavigationGraphTileSchema.Entry(
        screenId = screenId,
        initialTiles = TileSchemaBuilderScope().apply(initialTiles).build(),
        initialEvents = EventSchemaBuilderScope().apply(initialEvents).build(),
        failureTiles = TileSchemaBuilderScope().apply(failureTiles).build(),
        failureEvents = EventSchemaBuilderScope().apply(failureEvents).build(),
        transition = transition,
        popTransition = popTransition,
        predictivePopTransition = predictivePopTransition,
    )
}

class NestedNavigationGraphEntryBuilderScope :
    GenericBuilderScope<NestedNavigationGraphTileSchema.Entry, NestedNavigationGraphEntryBuilder>() {

    fun entry(
        screenId: String,
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
            NestedNavigationGraphEntryBuilder(
                screenId = screenId,
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
