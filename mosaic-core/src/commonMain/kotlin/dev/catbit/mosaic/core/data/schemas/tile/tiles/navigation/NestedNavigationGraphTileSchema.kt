package dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("NestedNavigationGraph")
data class NestedNavigationGraphTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema,
    @SerialName("visibility") override val visibility: TileSchema.Visibility,
    @SerialName("navigatorId") val navigatorId: String,
    @SerialName("entries") val entries: List<Entry>,
    @SerialName("startEntryId") val startEntryId: String
) : TileSchema {

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
        val failureEvents: List<EventSchema>
    )
}
