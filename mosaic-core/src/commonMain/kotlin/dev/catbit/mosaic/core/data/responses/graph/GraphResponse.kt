package dev.catbit.mosaic.core.data.responses.graph

import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The raw wire shape of a `Graph`/navigation-graph fetch response, decoded directly from the
 * backend's JSON — what `mosaic-server`'s `Graph { }` builder produces. Converted to a
 * [dev.catbit.mosaic.core.data.models.graph.GraphModel] via `GraphModel.fromGraphResponse` before the
 * client uses it further; that conversion is also where [ttl] is consumed (by the repository layer,
 * to decide cache freshness) and dropped — [dev.catbit.mosaic.core.data.models.graph.GraphModel]
 * itself carries no `ttl` field.
 *
 * @property entries every screen registered in this graph, each with its own loading/failure
 * content and transitions.
 * @property startEntryId id of the entry the graph opens on.
 * @property ttl how long this response may be served from cache before a fresh fetch is attempted —
 * an ISO date-time string, parsed and consumed only at the repository layer.
 * @property defaultTransition fallback enter/exit transition for any entry that doesn't declare its
 * own.
 * @property defaultPopTransition fallback transition for popping the back stack.
 * @property defaultPredictivePopTransition fallback transition for the predictive back gesture.
 */
@Serializable
data class GraphResponse(
    @SerialName("entries")
    val entries: SerializableImmutableList<Entry>,
    @SerialName("startEntryId")
    val startEntryId: String,
    @SerialName("ttl")
    val ttl: String? = null,
    @SerialName("defaultTransition")
    val defaultTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPopTransition")
    val defaultPopTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPredictivePopTransition")
    val defaultPredictivePopTransition: ContentTransitionSchema? = null,
) {

    /**
     * One registered screen within the graph — the wire shape of a server DSL `entry { }` block.
     *
     * @property screenId id of the screen this entry represents.
     * @property initialTiles tiles shown while the screen's real content is loading.
     * @property initialEvents events run while loading — defaults to the `GetScreen`→
     * `ChangeScreenState` pair in the DSL, but arrives here already resolved to a concrete list.
     * @property failureTiles tiles shown if loading fails.
     * @property failureEvents events run on failure.
     * @property transition enter/exit transition for navigating to this entry, overriding the
     * graph's own default.
     * @property popTransition transition for popping back off this entry.
     * @property predictivePopTransition transition for the predictive back gesture on this entry.
     */
    @Serializable
    data class Entry(
        @SerialName("screenId")
        val screenId: String,
        @SerialName("initialTiles")
        val initialTiles: SerializableImmutableList<TileSchema>,
        @SerialName("initialEvents")
        val initialEvents: SerializableImmutableList<EventSchema>,
        @SerialName("failureTiles")
        val failureTiles: SerializableImmutableList<TileSchema>,
        @SerialName("failureEvents")
        val failureEvents: SerializableImmutableList<EventSchema>,
        @SerialName("transition")
        val transition: ContentTransitionSchema? = null,
        @SerialName("popTransition")
        val popTransition: ContentTransitionSchema? = null,
        @SerialName("predictivePopTransition")
        val predictivePopTransition: ContentTransitionSchema? = null,
    )
}
