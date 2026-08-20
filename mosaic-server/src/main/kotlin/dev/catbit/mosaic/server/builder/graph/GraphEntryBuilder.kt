package dev.catbit.mosaic.server.builder.graph

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse.Entry
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.server.builder.GenericBuilder
import dev.catbit.mosaic.server.builder.GenericBuilderScope
import dev.catbit.mosaic.server.builder.event.EventSchemaBuilderScope
import dev.catbit.mosaic.server.builder.event.builders.screen.ChangeScreenState
import dev.catbit.mosaic.server.builder.event.builders.screen.GetScreen
import dev.catbit.mosaic.server.builder.event.builders.screen.successState
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

/** Compiles one navigation graph entry's tiles, events and transitions into an [Entry]. */
class GraphEntryBuilder(
    private val screenId: String,
    private val initialTiles: TileSchemaBuilderScope.() -> Unit = {},
    private val initialEvents: EventSchemaBuilderScope.() -> Unit = {},
    private val failureTiles: TileSchemaBuilderScope.() -> Unit = {},
    private val failureEvents: EventSchemaBuilderScope.() -> Unit = {},
    private val transition: ContentTransitionSchema? = null,
    private val popTransition: ContentTransitionSchema? = null,
    private val predictivePopTransition: ContentTransitionSchema? = null,
) : GenericBuilder<Entry>() {

    override fun build() = Entry(
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

/** Scope collecting every [Entry] declared inside a [dev.catbit.mosaic.server.builder.graph.Graph]'s `entries` block. */
class GraphEntryBuilderScope : GenericBuilderScope<Entry, GraphEntryBuilder>() {

    /**
     * Declares one screen reachable inside a [dev.catbit.mosaic.server.builder.graph.Graph].
     *
     * @param screenId Identifier of this entry, matched against the graph's `startEntryId` and against `Navigate` destinations.
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
            GraphEntryBuilder(
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