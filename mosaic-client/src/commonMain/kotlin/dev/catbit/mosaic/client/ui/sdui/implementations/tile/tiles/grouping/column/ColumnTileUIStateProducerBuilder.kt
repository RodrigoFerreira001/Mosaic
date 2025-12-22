package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column

import dev.catbit.mosaic.client.extensions.extractAndPutIfPresent
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.tile.GroupingTileUIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.placement.AlignmentModel
import dev.catbit.mosaic.core.data.tile.placement.ArrangementModel
import dev.catbit.mosaic.core.data.tile.tiles.grouping.ColumnTileModel

object ColumnTileUIStateProducerBuilder :
    GroupingTileUIStateProducerBuilder<ColumnTileModel, ColumnTileUIStateProducer> {

    override fun UIStateProducerBuilderScope.buildTile(data: ColumnTileModel) = with(data) {
        ColumnTileUIStateProducer(
            id = id,
            visibility = visibility.mapTo(),
            style = buildProducer(style),
            tiles = tiles.map { buildProducer<TileUIStateProducer<*>>(it) }.toMutableList(),
            arrangement = arrangement.mapTo(),
            alignment = alignment.mapTo(),
            updater = { updateData ->
                mutableMapOf<String, Any?>().apply {
                    extractAndPutIfPresent(ColumnTileModel::arrangement.name, updateData) {
                        decode<ArrangementModel.Vertical>(it).mapTo()
                    }
                    extractAndPutIfPresent(ColumnTileModel::alignment.name, updateData) {
                        decode<AlignmentModel.Horizontal>(it).mapTo()
                    }
                    extractAndPutIfPresent(ColumnTileModel::visibility.name, updateData) {
                        decode<TileModel.Visibility>(it).mapTo()
                    }
                    extractAndPutIfPresent(ColumnTileModel::style.name, updateData)
                }
            },
        )
    }
}