package dev.catbit.mosaic.server.builder.tile.builders.navigation

import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NestedNavigationGraphTileSchema
import dev.catbit.mosaic.core.extensions.randomId
import kotlinx.collections.immutable.toImmutableList
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.GetScreen
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.style.StyleSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.visible

internal class NestedNavigationGraphTileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
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
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        navigatorId = navigatorId,
        entries = NestedNavigationGraphEntryBuilderScope().apply(entries).build(),
        startEntryId = startEntryId,
        defaultTransition = defaultTransition,
        defaultPopTransition = defaultPopTransition,
        defaultPredictivePopTransition = defaultPredictivePopTransition,
    )
}

/**
 * Hosts a self-contained navigation back stack inside a tile, so a region of a screen can
 * navigate independently of the rest. The stack starts at [startEntryId] and every screen it can
 * show is declared via [entries] (built with `entry`). On first composition the tile registers
 * itself under [navigatorId] — that id is how `Navigate` events address this graph — and that
 * registration (along with each entry) is torn down when the tile leaves composition, so the
 * graph and its entries only exist while the tile is on screen; each entry gets its own state
 * holder, so screen state survives navigating back and forth within the graph. The system back
 * gesture pops this graph's own stack, not the outer one. For each navigation, the entry's own
 * [Entry] transitions win; when an entry does not define one, [defaultTransition] /
 * [defaultPopTransition] / [defaultPredictivePopTransition] apply; when neither is set the
 * navigation is instantaneous. Dispatches `onNavigationEntrySet(screenId)` whenever an entry is
 * displayed — including the start destination and every time navigation returns to an entry — so
 * events can be wired per destination.
 *
 * @param id Unique identifier of the tile. Defaults to a random id.
 * @param events Events owned by this tile, wired to its triggers (e.g. `onNavigationEntrySet`).
 * @param style Layout/appearance modifiers (size, padding, background, etc).
 * @param visibility Whether the tile is shown, hidden but occupies space, or removed from layout. Defaults to visible.
 * @param searchableTerms Terms used by an ancestor's search/filter to decide whether this tile matches. Defaults to none.
 * @param navigatorId Identifier this graph registers under, used by `Navigate` events to target it.
 * @param startEntryId Screen id of the entry the stack starts at.
 * @param defaultTransition Fallback enter/exit transition used by entries that don't declare their own. Defaults to none (instantaneous).
 * @param defaultPopTransition Fallback back-navigation transition used by entries that don't declare their own. Defaults to none (instantaneous).
 * @param defaultPredictivePopTransition Fallback predictive-back-gesture transition used by entries that don't declare their own. Defaults to none (instantaneous).
 * @param entries Screens reachable inside this graph, declared with `entry`.
 */
fun TileSchemaBuilderScope.NestedNavigationGraph(
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    visibility: TileSchema.Visibility = visible(),
    searchableTerms: List<String>? = null,
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
            searchableTerms = searchableTerms,
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

    /**
     * Declares one screen reachable inside a `NestedNavigationGraph`.
     *
     * @param screenId Identifier of this entry, matched against `startEntryId` and against `Navigate` destinations targeting this graph's `navigatorId`.
     * @param initialTiles Tiles shown while this entry's initial data is loading. Defaults to none.
     * @param initialEvents Events run when this entry becomes displayed. Defaults to `GetScreen` on display, followed by `ChangeScreenState` to success on success.
     * @param failureTiles Tiles shown if loading this entry fails. Defaults to none.
     * @param failureEvents Events run when loading this entry fails (e.g. a retry action). Defaults to none.
     * @param transition Enter/exit transition used when navigating to this entry. Defaults to none (falls back to the graph's `defaultTransition`).
     * @param popTransition Transition used when navigating back from this entry. Defaults to none (falls back to the graph's `defaultPopTransition`).
     * @param predictivePopTransition Transition used during the predictive back gesture on this entry. Defaults to none (falls back to the graph's `defaultPredictivePopTransition`).
     */
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
