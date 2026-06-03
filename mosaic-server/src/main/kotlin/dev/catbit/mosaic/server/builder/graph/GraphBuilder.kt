package dev.catbit.mosaic.server.builder.graph

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.server.builder.GenericBuilder
import kotlinx.datetime.LocalDateTime

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