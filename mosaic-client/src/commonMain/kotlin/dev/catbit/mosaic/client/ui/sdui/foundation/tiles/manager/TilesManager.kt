package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.UpdateScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEventDispatcher
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesOverlaysEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesStateUpdater
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileHolder
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileModel
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.event_trigger.EventTrigger
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.extensions.runSafely
import dev.catbit.mosaic.core.extensions.withNotNull
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import org.koin.core.scope.Scope

class TilesManager(
    private val tileHolderBuilderManager: TileHolderBuilderManager,
    private val eventHolderBuilderManager: EventHolderBuilderManager,
    serializer: MosaicSerializer,
    koinScope: Scope
) : TilesEditor,
    TilesStateUpdater,
    TilesEventDispatcher,
    TilesOverlaysEditor,
    TilesEventHolder {

    private val screenTileHolerId = "mosaic::root"

    private var onUpdateRequest: (TileModel) -> Unit = {}
    private lateinit var screenTileHolder: ScreenTileHolder

    private val builderScope by lazy {
        BuilderScope(
            tileHolderBuilderManager = tileHolderBuilderManager,
            eventHolderBuilderManager = eventHolderBuilderManager,
            serializer = serializer,
            koinScope = koinScope
        )
    }

    private val updateScope by lazy {
        UpdateScope(
            serializer = serializer
        )
    }

    fun setup(
        tiles: List<TileModel>,
        navigationDrawerTiles: List<TileModel>?,
        events: List<EventModel>?,
        onUpdateStateRequest: (TileModel) -> Unit
    ) {
        runSafely {
            onUpdateRequest = onUpdateStateRequest
            screenTileHolder = with(tileHolderBuilderManager) {
                builderScope.build(
                    ScreenTileModel(
                        id = screenTileHolerId,
                        tiles = tiles,
                        navigationDrawerTiles = navigationDrawerTiles,
                        events = events,
                    )
                )
            } as ScreenTileHolder
            updateState()
        }
    }

    override fun updateState() {
        onUpdateRequest(screenTileHolder.get())
    }

    override fun addTile(
        tileModel: TileModel,
        where: InsertionPosition
    ) {
        runSafely {
            screenTileHolder.addChild(
                child = with(tileHolderBuilderManager) {
                    builderScope.build(tileModel)
                },
                where = where
            )
            updateState()
        }
    }

    override fun addTiles(
        tileModels: List<TileModel>,
        where: InsertionPosition
    ) {
        runSafely {
            screenTileHolder.addChildren(
                children = with(tileHolderBuilderManager) {
                    tileModels.map { tileModel ->
                        builderScope.build(tileModel)
                    }
                },
                where = where
            )
            updateState()
        }
    }

    override fun addTile(
        tileModel: TileModel,
        groupingTileId: String,
        where: InsertionPosition
    ) {
        runSafely {
            screenTileHolder.getTileHolder(groupingTileId)?.addChild(
                child = with(tileHolderBuilderManager) {
                    builderScope.build(tileModel)
                },
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
        runSafely {
            screenTileHolder.getTileHolder(groupingTileId)?.addChildren(
                children = with(tileHolderBuilderManager) {
                    tileModels.map { tileModel ->
                        builderScope.build(tileModel)
                    }
                },
                where = where
            )
            updateState()
        }
    }

    override fun removeTile(
        tileId: String,
        groupingTileId: String?
    ) {
        runSafely {
            screenTileHolder.getTileHolder(
                tileId = groupingTileId ?: screenTileHolerId
            )?.removeChild(tileId)
            updateState()
        }
    }

    override fun removeTiles(
        tileIds: List<String>,
        groupingTileId: String?
    ) {
        runSafely {
            screenTileHolder.getTileHolder(
                tileId = groupingTileId ?: screenTileHolerId
            )?.removeChildren(tileIds)
            updateState()
        }
    }

    override fun replaceTiles(
        tileModels: List<TileModel>,
        groupingTileId: String?
    ) {
        runSafely {
            screenTileHolder.getTileHolder(
                tileId = groupingTileId ?: screenTileHolerId
            )?.apply {
                wipeChildren()
                addChildren(
                    children = with(tileHolderBuilderManager) {
                        tileModels.map { tileModel ->
                            builderScope.build(tileModel)
                        }
                    }
                )
            }
            updateState()
        }
    }

    override fun wipeTiles(
        groupingTileId: String
    ) {
        runSafely {
            screenTileHolder.getTileHolder(
                tileId = groupingTileId
            )?.wipeChildren()
            updateState()
        }
    }

    override fun updateTile(
        tileId: String,
        updateData: Map<String, Any?>
    ) {
        runSafely {
            withNotNull(screenTileHolder.getTileHolder(tileId, includeEventsOnSearch = true)) {
                updateScope.update(updateData)
            }
            updateState()
        }
    }

    override fun onEvent(
        tileId: String,
        event: TileEvent
    ) {
        runSafely {
            screenTileHolder.getTileHolder(
                tileId = tileId
            )?.onTileEvent(event)
            updateState()
        }
    }

    override fun setBottomSheetTiles(
        tileModels: List<TileModel>
    ) {
        runSafely {
            screenTileHolder.setBottomSheetTiles(
                with(tileHolderBuilderManager) {
                    tileModels.map { tileModel ->
                        builderScope.build(tileModel)
                    }
                }
            )
            updateState()
        }
    }

    override fun setDialogTiles(
        tileModels: List<TileModel>
    ) {
        runSafely {
            screenTileHolder.setDialogTiles(
                with(tileHolderBuilderManager) {
                    tileModels.map { tileModel ->
                        builderScope.build(tileModel)
                    }
                }
            )
            updateState()
        }
    }

    override fun getEventsByTrigger(
        eventTrigger: EventTrigger
    ): List<EventModel>? = screenTileHolder.getEventsByTrigger(eventTrigger)
}