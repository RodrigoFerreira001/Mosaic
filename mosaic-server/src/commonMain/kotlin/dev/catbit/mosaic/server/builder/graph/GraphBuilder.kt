package dev.catbit.mosaic.server.builder.graph

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.server.builder.GenericBuilder
import kotlinx.datetime.LocalDateTime

internal class GraphBuilder(
    val entries: GraphEntryBuilderScope.() -> Unit,
    val startEntryId: String,
    val ttl: LocalDateTime? = null
) : GenericBuilder<GraphResponse> {

    override fun build() = GraphResponse(
        entries = GraphEntryBuilderScope().apply(entries).build(),
        startEntryId = startEntryId,
        ttl = ttl?.toString()
    )
}

fun Graph(
    entries: GraphEntryBuilderScope.() -> Unit,
    startEntryId: String,
    ttl: LocalDateTime? = null
) = GraphBuilder(
    entries = entries,
    startEntryId = startEntryId,
    ttl = ttl
).build()