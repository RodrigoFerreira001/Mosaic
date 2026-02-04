package dev.catbit.mosaic.core.data.models.graph

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GraphModel(
    @SerialName("entries")
    val entries: List<Entry>,
    @SerialName("startEntryId")
    val startEntryId: String
) {

    // TODO Definir animações https://youtu.be/qJMMc9oK3X8?list=PLQkwcJG4YTCRjyfVKB8vcK7zeC1BNmBz4
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

    companion object {
        fun fromGraphResponse(
            graphResponse: GraphResponse
        ) = GraphModel(
            startEntryId = graphResponse.startEntryId,
            entries = graphResponse.entries.map { entry ->
                with(entry) {
                    Entry(
                        screenId = screenId,
                        initialTiles = initialTiles,
                        initialEvents = initialEvents,
                        failureTiles = failureTiles,
                        failureEvents = failureEvents
                    )
                }
            }
        )
    }
}
