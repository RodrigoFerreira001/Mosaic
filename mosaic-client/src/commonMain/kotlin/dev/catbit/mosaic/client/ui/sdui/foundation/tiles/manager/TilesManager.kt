package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager

import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.models.InsertionPosition
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.BuilderScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.TileEventScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.UpdateScope
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEventDispatcher
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesEventHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesOverlaysEditor
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesStateUpdater
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors.TilesValueProducer
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
    private val parent: TilesManager?,
    private val tileHolderBuilderManager: TileHolderBuilderManager,
    private val eventHolderBuilderManager: EventHolderBuilderManager,
    serializer: MosaicSerializer,
    koinScope: Scope
) : TilesEditor,
    TilesStateUpdater,
    TilesEventDispatcher,
    TilesOverlaysEditor,
    TilesEventHolder,
    TilesValueProducer {

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

    private val tileEventScope by lazy {
        TileEventScope(
            builderScope = builderScope
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

    override fun addTile(
        tileSchema: TileSchema,
        groupingTileId: String,
        where: InsertionPosition
    ) {
        runSafely {
            getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
                tileHolder.addChild(
                    child = with(tileHolderBuilderManager) {
                        builderScope.build(tileSchema)
                    },
                    where = where
                )
                owner.updateState()
            }
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

    override fun addTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
        where: InsertionPosition
    ) {
        runSafely {
            getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
                tileHolder.addChildren(
                    children = with(tileHolderBuilderManager) {
                        tileSchemas.map { tileSchema ->
                            builderScope.build(tileSchema)
                        }
                    },
                    where = where
                )
                owner.updateState()
            }
        }
    }

    override fun removeTile(
        tileId: String
    ) {
        runSafely {
            screenTileHolder.getTileHolder(screenTileHolerId)?.removeChild(tileId)
            updateState()
        }
    }

    override fun removeTile(
        tileId: String,
        groupingTileId: String
    ) {
        runSafely {
            getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
                tileHolder.removeChild(tileId)
                owner.updateState()
            }
        }
    }

    override fun removeTiles(
        tileIds: List<String>
    ) {
        runSafely {
            screenTileHolder.getTileHolder(screenTileHolerId)?.removeChildren(tileIds)
            updateState()
        }
    }

    override fun removeTiles(
        tileIds: List<String>,
        groupingTileId: String
    ) {
        runSafely {
            getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
                tileHolder.removeChildren(tileIds)
                owner.updateState()
            }
        }
    }

    override fun replaceTiles(
        tileSchemas: List<TileSchema>
    ) {
        runSafely {
            screenTileHolder.getTileHolder(screenTileHolerId)?.apply {
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

    override fun replaceTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String
    ) {
        runSafely {
            getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
                tileHolder.apply {
                    wipeChildren()
                    addChildren(
                        children = with(tileHolderBuilderManager) {
                            tileSchemas.map { tileSchema ->
                                builderScope.build(tileSchema)
                            }
                        }
                    )
                }
                owner.updateState()
            }
        }
    }

    override fun wipeTiles(
        groupingTileId: String
    ) {
        runSafely {
            getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
                tileHolder.wipeChildren()
                owner.updateState()
            }
        }
    }

    override fun updateTile(
        tileId: String,
        updateData: Map<String, Any?>
    ) {
        runSafely {
            getTileHolderAndOwner(
                tileId = tileId,
                includeEventsOnSearch = true
            )?.let { (tileHolder, owner) ->
                with(tileHolder) {
                    updateScope.update(updateData)
                }
                owner.updateState()
            }
        }
    }

    override fun onEvent(
        tileId: String,
        event: TileEvent
    ) {
        runSafely {
            getTileHolderAndOwner(tileId)?.let { (tileHolder, owner) ->
                with(tileHolder) {
                    tileEventScope.onTileEvent(event)
                }
                owner.updateState()
            }
        }
    }

    override fun onGroupEvent(
        event: TileGroupEvent
    ) {
        runSafely {
            getTileHoldersByGroupEventAndOwner(event)?.let { (tileHolders, owner) ->
                tileHolders.forEach { tileHolder ->
                    with(tileHolder) {
                        tileEventScope.onTileGroupEvent(event)
                    }
                }
                owner.updateState()
            }
        }
    }

    override fun getEventSchema(
        eventId: String
    ): EventSchema? = screenTileHolder.getEventHolder(eventId)?.get() ?: parent?.getEventSchema(eventId)

    override fun updateEventHolder(
        eventId: String,
        data: Map<String, Any?>
    ) {
        runSafely {
            getEventHolderAndOwner(eventId)?.let { (eventHolder, owner) ->
                with(eventHolder) {
                    updateScope.update(data)
                }
                owner.updateState()
            }
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

    override fun getValueWithKey(
        tileId: String,
        key: String
    ): Map<String, Any>? = screenTileHolder.getTileHolder(tileId)?.produceValueWithKey(key)

    fun getTileHolderAndOwner(
        tileId: String,
        includeEventsOnSearch: Boolean = false
    ): Pair<TileHolder<*>, TilesManager>? {
        return screenTileHolder.getTileHolder(tileId, includeEventsOnSearch)?.let { tileHolder ->
            tileHolder to this
        } ?: parent?.getTileHolderAndOwner(tileId, includeEventsOnSearch)
    }

    fun getTileHoldersByGroupEventAndOwner(
        event: TileGroupEvent
    ): Pair<List<TileHolder<*>>, TilesManager>? {
        return screenTileHolder.getTileHoldersByGroupEvent(event).takeIf { it.isNotEmpty() }?.let { tileHolders ->
            tileHolders to this
        } ?: parent?.getTileHoldersByGroupEventAndOwner(event)
    }

    fun getEventHolderAndOwner(
        eventId: String
    ): Pair<EventHolder<*>, TilesManager>? {
        return screenTileHolder.getEventHolder(eventId)?.let { eventHolder ->
            eventHolder to this
        } ?: parent?.getEventHolderAndOwner(eventId)
    }
}