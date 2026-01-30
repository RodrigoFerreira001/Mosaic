package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.GroupingTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.style.StyleSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenTileSchema(
    @SerialName("id") override val id: String,
    @SerialName("tiles") override val tiles: List<TileSchema>,
    @SerialName("events") override val events: List<EventSchema>?,
    @SerialName("style") override val style: StyleSchema = StyleSchema.default(),
    @SerialName("visibility") override val visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    @SerialName("navigationDrawerTiles") val navigationDrawerTiles: List<TileSchema>?,
    @SerialName("currentBottomSheetTiles") val currentBottomSheetTiles: List<TileSchema>? = null,
    @SerialName("currentDialogSheetTiles") val currentDialogSheetTiles: List<TileSchema>? = null
) : GroupingTileSchema