package dev.catbit.mosaic.client.ui.sdui.foundation.state.manager

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRegister
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.GroupingTileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducer
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.tile.TileUIStateProducerScope
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.extensions.withNotNull
import dev.catbit.mosaic.core.mapping.Mapper
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.scope.Scope
import kotlin.reflect.KClass

class TilesUIStateManager(
    uiStateProducerBuilders: Map<KClass<*>, UIStateProducerBuilder<*, *>>,
    mapper: Mapper,
    serializer: MosaicSerializer,
    koinScope: Scope
) : TilesEditor, TilesStateUpdater {
    private lateinit var eventRegister: EventRegister

    fun attachEventRegister(eventRegister: EventRegister) {
        this.eventRegister = eventRegister
    }

    private val internalUIState = MutableStateFlow<List<TileUIState>>(listOf())
    val uiState get() = internalUIState.asStateFlow()

    private val tileUIStateProducers = mutableListOf<TileUIStateProducer<*>>()

    private val uiStateProducerBuilderScope by lazy {
        UIStateProducerBuilderScope(
            uiStateProducerBuilders = uiStateProducerBuilders,
            mapper = mapper,
            serializer = serializer,
            koinScope = koinScope,
            eventRegister = eventRegister
        )
    }

    private val tileUIStateProducerScope by lazy {
        TileUIStateProducerScope(
            tilesEditor = this,
            tilesStateUpdater = this,
        )
    }


    fun setup(tiles: List<TileModel>) {
        runCatching {
            tileUIStateProducers.apply {
                clear()
                addAll(
                    tiles.map { tile ->
                        uiStateProducerBuilderScope.buildProducer(tile)
                    }
                )
            }
            updateState()
        }
    }

    override fun updateState() {
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

    override fun addTile(
        tileModel: TileModel,
        where: InsertionPosition
    ) {
        runCatching {
            tileUIStateProducers.add(
                element = uiStateProducerBuilderScope.buildProducer(tileModel),
                index = where.toIndex(tileUIStateProducers)
            )

            updateState()
        }
    }

    override fun addTiles(
        tileModels: List<TileModel>,
        where: InsertionPosition
    ) {
        runCatching {
            tileUIStateProducers.addAll(
                elements = tileModels.map { tileModel ->
                    uiStateProducerBuilderScope.buildProducer(tileModel)
                },
                index = where.toIndex(tileUIStateProducers)
            )
            updateState()
        }
    }

    override fun addTile(
        tileModel: TileModel,
        groupingTileId: String,
        where: InsertionPosition
    ) {
        runCatching {
            (getTile(groupingTileId) as? GroupingTileUIStateProducer)
                ?.addChild(
                    child = uiStateProducerBuilderScope.buildProducer(tileModel),
                    where = where
                )

            updateState()
        }
    }

    override fun addTiles(
        tileModels: List<TileModel>,
        groupingTileId: String,
        where: InsertionPosition
    ) {
        runCatching {
            withNotNull(getTile(groupingTileId) as? GroupingTileUIStateProducer) {
                addChildren(
                    children = tileModels.map { tileModel ->
                        uiStateProducerBuilderScope.buildProducer(tileModel)
                    },
                    where = where
                )
            }

            updateState()
        }
    }

    override fun updateTile(
        id: String,
        updateData: Map<String, Any?>
    ) {
        getTile(id)?.update(updateData)
        updateState()
    }

    override fun removeTile(
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

    override fun removeTiles(
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

    override fun wipeTiles() {
        tileUIStateProducers.clear()
        updateState()
    }

    override fun wipeTiles(
        groupingTileId: String
    ) {
        (getTile(groupingTileId) as? GroupingTileUIStateProducer)?.wipeChildren()
        updateState()
    }

    fun onEvent(
        tileId: String,
        event: TileEvent
    ) {
        withNotNull(getTile(tileId)) {
            tileUIStateProducerScope.onEvent(event)
        }
    }
}