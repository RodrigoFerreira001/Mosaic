package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
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
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileSchema
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
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

    private var onUpdateRequest: (TileSchema) -> Unit = {}
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
        tiles: List<TileSchema>,
        navigationDrawerTiles: List<TileSchema>? = null,
        events: List<EventSchema>?,
        onUpdateStateRequest: (TileSchema) -> Unit
    ) {
        runSafely {
            onUpdateRequest = onUpdateStateRequest
            screenTileHolder = with(tileHolderBuilderManager) {
                builderScope.build(
                    ScreenTileSchema(
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
        tileSchema: TileSchema,
        where: InsertionPosition
    ) {
        runSafely {
            screenTileHolder.addChild(
                child = with(tileHolderBuilderManager) {
                    builderScope.build(tileSchema)
                },
                where = where
            )
            updateState()
        }
    }

    override fun addTiles(
        tileSchemas: List<TileSchema>,
        where: InsertionPosition
    ) {
        runSafely {
            screenTileHolder.addChildren(
                children = with(tileHolderBuilderManager) {
                    tileSchemas.map { tileSchema ->
                        builderScope.build(tileSchema)
                    }
                },
                where = where
            )
            updateState()
        }
    }

    override fun addTile(
        tileSchema: TileSchema,
        groupingTileId: String,
        where: InsertionPosition
    ) {
        runSafely {
            screenTileHolder.getTileHolder(groupingTileId)?.addChild(
                child = with(tileHolderBuilderManager) {
                    builderScope.build(tileSchema)
                },
                where = where
            )
            updateState()
        }
    }

    override fun addTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
        where: InsertionPosition
    ) {
        runSafely {
            screenTileHolder.getTileHolder(groupingTileId)?.addChildren(
                children = with(tileHolderBuilderManager) {
                    tileSchemas.map { tileSchema ->
                        builderScope.build(tileSchema)
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
        tileSchemas: List<TileSchema>,
        groupingTileId: String?
    ) {
        runSafely {
            screenTileHolder.getTileHolder(
                tileId = groupingTileId ?: screenTileHolerId
            )?.apply {
                wipeChildren()
                addChildren(
                    children = with(tileHolderBuilderManager) {
                        tileSchemas.map { tileSchema ->
                            builderScope.build(tileSchema)
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

    override fun onGroupEvent(
        event: TileGroupEvent
    ) {
        runSafely {
            screenTileHolder.getTileHoldersByGroupEvent(event).forEach { tileHolder ->
                tileHolder.onTileGroupEvent(event)
            }
            updateState()
        }
    }

    override fun setBottomSheetTiles(
        tileSchemas: List<TileSchema>
    ) {
        runSafely {
            screenTileHolder.setBottomSheetTiles(
                with(tileHolderBuilderManager) {
                    tileSchemas.map { tileSchema ->
                        builderScope.build(tileSchema)
                    }
                }
            )
            updateState()
        }
    }

    override fun setDialogTiles(
        tileSchemas: List<TileSchema>
    ) {
        runSafely {
            screenTileHolder.setDialogTiles(
                with(tileHolderBuilderManager) {
                    tileSchemas.map { tileSchema ->
                        builderScope.build(tileSchema)
                    }
                }
            )
            updateState()
        }
    }

    override fun getEventsByTrigger(
        eventTrigger: EventTrigger
    ): List<EventSchema>? = screenTileHolder.getEventsByTrigger(eventTrigger)
}