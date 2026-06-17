package dev.catbit.mosaic.core.data.responses.graph

import dev.catbit.mosaic.core.data.schemas.animation.ContentTransitionSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
