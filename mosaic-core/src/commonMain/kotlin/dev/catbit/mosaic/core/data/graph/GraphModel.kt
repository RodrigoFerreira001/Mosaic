package dev.catbit.mosaic.core.data.graph

import dev.catbit.mosaic.core.data.tile.TileModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GraphModel(
    @SerialName("id")
    val id: String,
    @SerialName("entries")
    val entries: List<Entry>,
    @SerialName("startEntryId")
    val startEntryId: String
) {

    // TODO Definir animações
    @Serializable
    data class Entry(
        @SerialName("screenId")
        val screenId: String,
        @SerialName("loadingTiles")
        val loadingTiles: List<TileModel>,
        @SerialName("errorTiles")
        val errorTiles: List<TileModel>
    )
}
