package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.exceptions.EventNotFoundException
import dev.catbit.mosaic.client.exceptions.TileNotFoundException
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
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.collections.immutable.ImmutableList
import org.koin.core.scope.Scope

@Stable
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
            serializer = serializer,
            builderScope = builderScope
        )
    }

    private val tileEventScope by lazy {
        TileEventScope(
            builderScope = builderScope
        )
    }

    fun setup(
        tiles: ImmutableList<TileSchema>,
        navigationDrawerTiles: ImmutableList<TileSchema>? = null,
        events: ImmutableList<EventSchema>?,
        onUpdateStateRequest: (TileSchema) -> Unit,
        state: ScreenTileSchema.State
    ) {
        runSafely(
            onError = { builderScope.logError(tag = "TilesManager.setup", throwable = it) }
        ) {
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
    ) = runCatching {
        screenTileHolder.addChild(
            child = with(tileHolderBuilderManager) {
                builderScope.build(tileSchema)
            },
            where = where
        )
        updateState()
    }

    override fun addTile(
        tileSchema: TileSchema,
        groupingTileId: String,
        where: InsertionPosition
    ) = runCatching {
        getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
            tileHolder.addChild(
                child = with(tileHolderBuilderManager) {
                    builderScope.build(tileSchema)
                },
                where = where
            )
            owner.updateState()
        } ?: throw TileNotFoundException("No tile with id '$groupingTileId' found")
    }

    override fun addTiles(
        tileSchemas: List<TileSchema>,
        where: InsertionPosition,
    ) = runCatching {
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

    override fun addTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
        where: InsertionPosition,
    ) = runCatching {
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
        } ?: throw TileNotFoundException("No tile with id '$groupingTileId' found")
    }

    override fun removeTile(
        tileId: String,
    ) = runCatching {
        screenTileHolder
            .getTileHolder(screenTileHolerId)
            ?.let {
                it.removeChild(tileId)
                updateState()
            } ?: throw TileNotFoundException("No tile with id '$tileId' found")
    }

    override fun removeTile(
        tileId: String,
        groupingTileId: String,
    ) = runCatching {
        getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
            tileHolder.removeChild(tileId)
            owner.updateState()
        } ?: throw TileNotFoundException("No tile with id '$tileId' found")
    }

    override fun removeTiles(
        tileIds: List<String>,
    ) = runCatching {
        screenTileHolder
            .getTileHolder(screenTileHolerId)
            ?.let {
                it.removeChildren(tileIds)
                updateState()
            } ?: throw TileNotFoundException("No tile with id '$screenTileHolerId' found")
    }


    override fun removeTiles(
        tileIds: List<String>,
        groupingTileId: String,
    ) = runCatching {
        getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
            tileHolder.removeChildren(tileIds)
            owner.updateState()
        } ?: throw TileNotFoundException("No tile with id '$groupingTileId' found")
    }

    override fun replaceTiles(
        tileSchemas: List<TileSchema>,
    ) = runCatching {
        screenTileHolder.getTileHolder(screenTileHolerId)?.let { holder ->
            holder.wipeChildren()
            holder.addChildren(
                children = with(tileHolderBuilderManager) {
                    tileSchemas.map { tileSchema ->
                        builderScope.build(tileSchema)
                    }
                }
            )
            updateState()
        } ?: throw TileNotFoundException("No tile with id '$screenTileHolerId' found")
    }

    override fun replaceTiles(
        tileSchemas: List<TileSchema>,
        groupingTileId: String,
    ) = runCatching {
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
        } ?: throw TileNotFoundException("No tile with id '$groupingTileId' found")
    }

    override fun wipeTiles(
        groupingTileId: String,
    ) = runCatching {
        getTileHolderAndOwner(groupingTileId)?.let { (tileHolder, owner) ->
            tileHolder.wipeChildren()
            owner.updateState()
        } ?: throw TileNotFoundException("No tile with id '$groupingTileId' found")
    }

    override fun updateTile(
        tileId: String,
        updateData: Map<String, Any?>,
    ) = runCatching {
        getTileHolderAndOwner(
            tileId = tileId,
            includeEventsOnSearch = true
        )?.let { (tileHolder, owner) ->
            with(tileHolder) {
                updateScope.update(updateData)
            }
            owner.updateState()
        } ?: throw TileNotFoundException("No tile with id '$tileId' found")
    }

    override fun checkIfTileHasChildren(
        groupingTileId: String,
        childrenIds: List<String>
    ) = getTileHolderAndOwner(
        tileId = groupingTileId,
        includeEventsOnSearch = true
    )?.let { (tileHolder, _) ->
        tileHolder.hasChildren(childrenIds)
    } == true

    override fun getTileChildrenCount(
        groupingTileId: String
    ) = getTileHolderAndOwner(
        tileId = groupingTileId,
        includeEventsOnSearch = true
    )?.let { (tileHolder, _) ->
        tileHolder.getChildrenCount()
    }

    override fun onEvent(
        tileId: String,
        event: TileEvent
    ) = runCatching {
        getTileHolderAndOwner(tileId)?.let { (tileHolder, owner) ->
            with(tileHolder) {
                tileEventScope.onTileEvent(event)
            }
            owner.updateState()
        } ?: throw TileNotFoundException("No tile with id '$tileId' found")
    }

    override fun onGroupEvent(
        event: TileGroupEvent,
    ) = runCatching {
        getTileHoldersByGroupEventAndOwner(event)?.let { (tileHolders, owner) ->
            tileHolders.forEach { tileHolder ->
                with(tileHolder) {
                    tileEventScope.onTileGroupEvent(event)
                }
            }
            owner.updateState()
        } ?: throw TileNotFoundException("No tiles found for group event '$event'")
    }

    override fun getEventSchema(
        eventId: String
    ): EventSchema? =
        screenTileHolder.getEventHolder(eventId)?.get() ?: parent?.getEventSchema(eventId)

    override fun updateEventHolder(
        eventId: String,
        data: Map<String, Any?>
    ) = runCatching {
        getEventHolderAndOwner(eventId)?.let { (eventHolder, owner) ->
            with(eventHolder) {
                updateScope.update(data)
            }
            owner.updateState()
        } ?: throw EventNotFoundException("No event with id '$eventId' found")
    }

    override fun setBottomSheetTiles(
        tileSchemas: List<TileSchema>
    ) {
        runSafely(
            onError = { builderScope.logError(tag = "TilesManager.setBottomSheetTiles", throwable = it) }
        ) {
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
        runSafely(
            onError = { builderScope.logError(tag = "TilesManager.setDialogTiles", throwable = it) }
        ) {
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