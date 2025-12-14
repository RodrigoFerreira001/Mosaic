package dev.catbit.mosaic.client.ui.foundation.state.manager

import dev.catbit.mosaic.client.ui.foundation.state.producer.builder.UIStateProducerBuilder
import dev.catbit.mosaic.client.ui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.foundation.state.producer.tile.GroupingTileUIStateProducer
import dev.catbit.mosaic.client.ui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.extensions.withNotNull
import dev.catbit.mosaic.core.mapping.Mapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class TilesUIStateManager(
    uiStateProducerBuilders: List<UIStateProducerBuilder<*, *>>,
    private val mapper: Mapper,
    private val json: Json
) {

    private val internalUIState = MutableStateFlow<List<TileUIState>>(listOf())
    val uiState get() = internalUIState.asStateFlow()

    private val tileUIStateProducers = mutableListOf<TileUIStateProducer<*>>()

    private val uiStateProducerBuilderScope = UIStateProducerBuilderScope(
        producers = uiStateProducerBuilders,
        mapper = mapper,
        json = json
    )

    fun setup(tiles: List<TileModel>) {
        runCatching {
            tileUIStateProducers.addAll(
                tiles.map { tile ->
                    uiStateProducerBuilderScope.buildProducer(tile)
                }
            )
        }
    }

    private fun updateState() {
        internalUIState.update {
            tileUIStateProducers.map { it.state }
        }
    }

    private fun getTile(
        id: String
    ) = tileUIStateProducers.firstNotNullOfOrNull { producer ->
        if (producer.id == id) producer
        else if (producer is GroupingTileUIStateProducer) producer.getChild(id)
        else null
    }

    fun addTile(
        tileModel: TileModel
    ) {
        runCatching {
            tileUIStateProducers.add(
                uiStateProducerBuilderScope.buildProducer(tileModel)
            )

            updateState()
        }
    }

    fun addTiles(
        tileModels: List<TileModel>
    ) {
        runCatching {
            tileUIStateProducers.addAll(
                tileModels.map { tileModel ->
                    uiStateProducerBuilderScope.buildProducer(tileModel)
                }
            )
            updateState()
        }
    }

    fun addTile(
        tileModel: TileModel,
        groupingTileId: String
    ) {
        runCatching {
            (getTile(groupingTileId) as? GroupingTileUIStateProducer)
                ?.appendChild(uiStateProducerBuilderScope.buildProducer(tileModel))

            updateState()
        }
    }

    fun addTiles(
        tileModels: List<TileModel>,
        groupingTileId: String
    ) {
        runCatching {
            withNotNull(getTile(groupingTileId) as? GroupingTileUIStateProducer) {
                tileModels.forEach { tileModel ->
                    appendChild(uiStateProducerBuilderScope.buildProducer(tileModel))
                }
            }

            updateState()
        }
    }

    fun updateTile(
        id: String,
        updateData: Map<String, Any?>
    ) {
        getTile(id)?.update(updateData)
        updateState()
    }

    fun removeTile(
        id: String
    ) {
        if (tileUIStateProducers.any { it.id == id }) {
            tileUIStateProducers.removeAll { it.id == id }
        } else {
            tileUIStateProducers.firstNotNullOfOrNull {
                (it as? GroupingTileUIStateProducer)?.getParentOf(id)
            }?.removeChild(id)
        }

        updateState()
    }

    fun removeTiles(
        ids: List<String>
    ) {
        tileUIStateProducers.forEach { producer ->
            ids.forEach { id ->
                (producer as? GroupingTileUIStateProducer)?.getParentOf(id)?.removeChild(id)
            }
        }
        tileUIStateProducers.removeAll { it.id in ids }

        updateState()
    }

    fun wipeTiles() {
        tileUIStateProducers.clear()
        updateState()
    }

    fun wipeTiles(
        groupingTileId: String
    ) {
        (getTile(groupingTileId) as? GroupingTileUIStateProducer)?.wipeChildren()
        updateState()
    }
}