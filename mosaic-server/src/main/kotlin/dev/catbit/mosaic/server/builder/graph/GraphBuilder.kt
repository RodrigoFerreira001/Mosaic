package dev.catbit.mosaic.server.builder.graph

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.server.builder.GenericBuilder
import kotlinx.datetime.LocalDateTime

/** Compiles a top-level navigation graph's [entries] and default transitions into a [GraphResponse]. */
internal class GraphBuilder(
    val entries: GraphEntryBuilderScope.() -> Unit,
    val startEntryId: String,
    val ttl: LocalDateTime? = null,
    val defaultTransition: ContentTransitionSchema? = null,
    val defaultPopTransition: ContentTransitionSchema? = null,
    val defaultPredictivePopTransition: ContentTransitionSchema? = null,
) : GenericBuilder<GraphResponse>() {

    override fun build() = GraphResponse(
        entries = GraphEntryBuilderScope().apply(entries).build(),
        startEntryId = startEntryId,
        ttl = ttl?.toString(),
        defaultTransition = defaultTransition,
        defaultPopTransition = defaultPopTransition,
        defaultPredictivePopTransition = defaultPredictivePopTransition,
    )
}

/**
 * Builds a top-level navigation graph — a set of screens sharing one back stack, with the stack
 * starting at [startEntryId]. This is the app's (or a screen area's) root navigator; for a
 * back stack nested inside a single tile, use `NestedNavigationGraph` instead.
 *
 * @param startEntryId Screen id the back stack starts at.
 * @param ttl Cache expiry — the client re-fetches the graph after this datetime instead of using its cached copy. Defaults to none (cached indefinitely).
 * @param defaultTransition Fallback enter/exit transition for entries that don't declare their own. Defaults to none (instantaneous).
 * @param defaultPopTransition Fallback back-navigation transition for entries that don't declare their own. Defaults to none (instantaneous).
 * @param defaultPredictivePopTransition Fallback predictive-back-gesture transition for entries that don't declare their own. Defaults to none (instantaneous).
 * @param entries Screens reachable inside this graph, declared with `entry`.
 */
fun Graph(
    startEntryId: String,
    ttl: LocalDateTime? = null,
    defaultTransition: ContentTransitionSchema? = null,
    defaultPopTransition: ContentTransitionSchema? = null,
    defaultPredictivePopTransition: ContentTransitionSchema? = null,
    entries: GraphEntryBuilderScope.() -> Unit,
) = GraphBuilder(
    entries = entries,
    startEntryId = startEntryId,
    ttl = ttl,
    defaultTransition = defaultTransition,
    defaultPopTransition = defaultPopTransition,
    defaultPredictivePopTransition = defaultPredictivePopTransition,
).build()