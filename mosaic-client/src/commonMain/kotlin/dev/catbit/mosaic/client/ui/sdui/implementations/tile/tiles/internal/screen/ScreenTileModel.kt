package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.GroupingTileModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenTileModel(
    @SerialName("id") override val id: String,
    @SerialName("tiles") override val tiles: List<TileModel>,
    @SerialName("events") override val events: List<EventModel>?,
    @SerialName("style") override val style: StyleModel = StyleModel.default(),
    @SerialName("visibility") override val visibility: TileModel.Visibility = TileModel.Visibility.VISIBLE,
    @SerialName("navigationDrawerTiles") val navigationDrawerTiles: List<TileModel>?,
    @SerialName("currentBottomSheetTiles") val currentBottomSheetTiles: List<TileModel>? = null,
    @SerialName("currentDialogSheetTiles") val currentDialogSheetTiles: List<TileModel>? = null
) : GroupingTileModel