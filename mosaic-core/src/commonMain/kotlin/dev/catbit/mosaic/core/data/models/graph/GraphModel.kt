package dev.catbit.mosaic.core.data.models.graph

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.extensions.immutableMapTo
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The app-facing navigation graph — [dev.catbit.mosaic.core.data.responses.graph.GraphResponse] with
 * [dev.catbit.mosaic.core.data.responses.graph.GraphResponse.ttl] already consumed by the repository
 * layer and dropped. This is what `MosaicApplicationStateHolder.State.Displaying` carries, and what
 * `MosaicApplication`'s `NavDisplay` resolves transitions from.
 *
 * @property entries every screen registered in this graph.
 * @property startEntryId id of the entry the graph opens on.
 * @property defaultTransition fallback enter/exit transition for any entry that doesn't declare its
 * own.
 * @property defaultPopTransition fallback transition for popping the back stack.
 * @property defaultPredictivePopTransition fallback transition for the predictive back gesture.
 */
@Serializable
data class GraphModel(
    @SerialName("entries")
    val entries: SerializableImmutableList<Entry>,
    @SerialName("startEntryId")
    val startEntryId: String,
    @SerialName("defaultTransition")
    val defaultTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPopTransition")
    val defaultPopTransition: ContentTransitionSchema? = null,
    @SerialName("defaultPredictivePopTransition")
    val defaultPredictivePopTransition: ContentTransitionSchema? = null,
) {

    /**
     * One registered screen within the graph — the app-facing counterpart of
     * [dev.catbit.mosaic.core.data.responses.graph.GraphResponse.Entry], identical shape, staged by
     * `MosaicApplicationStateHolder` into `ScreenExtrasHolder` for whenever this screen actually
     * opens.
     *
     * @property screenId id of the screen this entry represents.
     * @property initialTiles tiles shown while the screen's real content is loading.
     * @property initialEvents events run while loading.
     * @property failureTiles tiles shown if loading fails.
     * @property failureEvents events run on failure.
     * @property transition enter/exit transition for navigating to this entry.
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

    companion object {
        /** Converts a [GraphResponse] into a [GraphModel], dropping [GraphResponse.ttl] (already
         * consumed by the repository layer by this point). */
        fun fromGraphResponse(
            graphResponse: GraphResponse
        ) = GraphModel(
            startEntryId = graphResponse.startEntryId,
            defaultTransition = graphResponse.defaultTransition,
            defaultPopTransition = graphResponse.defaultPopTransition,
            defaultPredictivePopTransition = graphResponse.defaultPredictivePopTransition,
            entries = graphResponse.entries.immutableMapTo { entry ->
                with(entry) {
                    Entry(
                        screenId = screenId,
                        initialTiles = initialTiles,
                        initialEvents = initialEvents,
                        failureTiles = failureTiles,
                        failureEvents = failureEvents,
                        transition = transition,
                        popTransition = popTransition,
                        predictivePopTransition = predictivePopTransition,
                    )
                }
            }
        )
    }
}
