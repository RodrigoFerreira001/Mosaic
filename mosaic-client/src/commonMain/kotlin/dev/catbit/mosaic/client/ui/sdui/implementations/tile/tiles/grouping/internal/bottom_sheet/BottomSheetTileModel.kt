package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.bottom_sheet

import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.GroupingTileModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.style.SizeModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("BottomSheet")
internal data class BottomSheetTileModel(
    override val id: String,
    override val events: List<EventModel>? = null,
    override val style: StyleModel = StyleModel(
        SizeModel(
            width = SizeModel.Behavior.Horizontal.Wrap,
            height = SizeModel.Behavior.Vertical.Wrap
        )
    ),
    override val visibility: TileModel.Visibility = TileModel.Visibility.VISIBLE,
    override val tiles: List<TileModel>,
    val isCancellable: Boolean,
) : GroupingTileModel