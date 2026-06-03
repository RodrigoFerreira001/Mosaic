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
import dev.catbit.mosaic.client.exceptions.EventNotFoundException
import dev.catbit.mosaic.client.exceptions.TileNotFoundException
import dev.catbit.mosaic.core.extensions.runSafely
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
        onUpdateStateRequest: (TileSchema) -> Unit,
        state: ScreenTileSchema.State
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
                        state = state
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
        where: InsertionPosition,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            screenTileHolder.addChild(
                child = with(tileHolderBuilderManager) {
                    builderScope.build(tileSchema)
                },
                where = where
            )
            updateState()
            onSuccess()
        }
    }

    override fun addTile(
        tileSchema: TileSchema,
        groupingTileId: String,
        where: InsertionPosition,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
                tileHolder.addChild(
                    child = with(tileHolderBuilderManager) {
                        builderScope.build(tileSchema)
                    },
                    where = where
                )
                owner.updateState()
                onSuccess()
            } ?: run {
                onError(TileNotFoundException("No tile with id '$groupingTileId' found"))
            }
        }
    }

    override fun addTiles(
        tileSchemas: List<TileSchema>,
        where: InsertionPosition,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            screenTileHolder.addChildren(
                children = with(tileHolderBuilderManager) {
                    tileSchemas.map { tileSchema ->
                        builderScope.build(tileSchema)
                    }
                },
                where = where
            )
            updateState()
            onSuccess()
        }
    }

    override fun addTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
        where: InsertionPosition,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
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
                onSuccess()
            } ?: run {
                onError(TileNotFoundException("No tile with id '$groupingTileId' found"))
            }
        }
    }

    override fun removeTile(
        tileId: String,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely {
            screenTileHolder
                .getTileHolder(screenTileHolerId)
                ?.let {
                    it.removeChild(tileId)
                    updateState()
                    onSuccess()
                } ?: run {
                onError(TileNotFoundException("No tile with id '$tileId' found"))
            }
        }
    }

    override fun removeTile(
        tileId: String,
        groupingTileId: String,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
                tileHolder.removeChild(tileId)
                owner.updateState()
                onSuccess()
            } ?: run {
                onError(TileNotFoundException("No tile with id '$tileId' found"))
            }
        }
    }

    override fun removeTiles(
        tileIds: List<String>,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            screenTileHolder
                .getTileHolder(screenTileHolerId)
                ?.let {
                    it.removeChildren(tileIds)
                    updateState()
                    onSuccess()
                } ?: run {
                onError(TileNotFoundException("No tile with id '$screenTileHolerId' found"))
            }
        }
    }

    override fun removeTiles(
        tileIds: List<String>,
        groupingTileId: String,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
                tileHolder.removeChildren(tileIds)
                owner.updateState()
            } ?: run {
                onError(TileNotFoundException("No tile with id '$groupingTileId' found"))
            }
        }
    }

    override fun replaceTiles(
        tileSchemas: List<TileSchema>,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            screenTileHolder.getTileHolder(screenTileHolerId)?.apply {
                wipeChildren()
                addChildren(
                    children = with(tileHolderBuilderManager) {
                        tileSchemas.map { tileSchema ->
                            builderScope.build(tileSchema)
                        }
                    }
                )
                updateState()
                onSuccess()
            } ?: run {
                onError(TileNotFoundException("No tile with id '$screenTileHolerId' found"))
            }
        }
    }

    override fun replaceTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
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
                onSuccess()
            } ?: run {
                onError(TileNotFoundException("No tile with id '$groupingTileId' found"))
            }
        }
    }

    override fun wipeTiles(
        groupingTileId: String,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
                tileHolder.wipeChildren()
                owner.updateState()
                onSuccess()
            } ?: run {
                onError(TileNotFoundException("No tile with id '$groupingTileId' found"))
            }
        }
    }

    override fun updateTile(
        tileId: String,
        updateData: Map<String, Any?>,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            getTileHolderAndOwner(
                tileId = tileId,
                includeEventsOnSearch = true
            )?.let { (tileHolder, owner) ->
                with(tileHolder) {
                    updateScope.update(updateData)
                }
                owner.updateState()
                onSuccess()
            } ?: run {
                onError(TileNotFoundException("No tile with id '$tileId' found"))
            }
        }
    }

    override fun onEvent(
        tileId: String,
        event: TileEvent,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            getTileHolderAndOwner(tileId)?.let { (tileHolder, owner) ->
                with(tileHolder) {
                    tileEventScope.onTileEvent(event)
                }
                owner.updateState()
                onSuccess()
            } ?: run {
                onError(TileNotFoundException("No tile with id '$tileId' found"))
            }
        }
    }

    override fun onGroupEvent(
        event: TileGroupEvent,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            getTileHoldersByGroupEventAndOwner(event)?.let { (tileHolders, owner) ->
                tileHolders.forEach { tileHolder ->
                    with(tileHolder) {
                        tileEventScope.onTileGroupEvent(event)
                    }
                }
                owner.updateState()
                onSuccess()
            } ?: run {
                onError(TileNotFoundException("No tiles found for group event '$event'"))
            }
        }
    }

    override fun getEventSchema(
        eventId: String
    ): EventSchema? =
        screenTileHolder.getEventHolder(eventId)?.get() ?: parent?.getEventSchema(eventId)

    override fun updateEventHolder(
        eventId: String,
        data: Map<String, Any?>,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        runSafely(
            onError = onError
        ) {
            getEventHolderAndOwner(eventId)?.let { (eventHolder, owner) ->
                with(eventHolder) {
                    updateScope.update(data)
                }
                owner.updateState()
                onSuccess()
            } ?: run {
                onError(EventNotFoundException("No event with id '$eventId' found"))
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
        return screenTileHolder.getTileHoldersByGroupEvent(event).takeIf { it.isNotEmpty() }
            ?.let { tileHolders ->
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