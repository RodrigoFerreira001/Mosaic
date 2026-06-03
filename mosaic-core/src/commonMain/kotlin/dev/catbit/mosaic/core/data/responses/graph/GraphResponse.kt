package dev.catbit.mosaic.core.data.responses.graph

import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GraphResponse(
    @SerialName("entries")
    val entries: List<Entry>,
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

    @Serializable
    data class Entry(
        @SerialName("screenId")
        val screenId: String,
        @SerialName("initialTiles")
        val initialTiles: List<TileSchema>,
        @SerialName("initialEvents")
        val initialEvents: List<EventSchema>,
        @SerialName("failureTiles")
        val failureTiles: List<TileSchema>,
        @SerialName("failureEvents")
        val failureEvents: List<EventSchema>,
        @SerialName("transition")
        val transition: ContentTransitionSchema? = null,
        @SerialName("popTransition")
        val popTransition: ContentTransitionSchema? = null,
        @SerialName("predictivePopTransition")
        val predictivePopTransition: ContentTransitionSchema? = null,
    )
}
