package dev.catbit.mosaic.core.data.responses.graph

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
    val ttl: String? = null
) {

    // TODO Definir animações
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
    )
}
