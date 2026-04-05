package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.base.ScreenStateHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.TilesManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRendererManager
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MosaicScreenStateHolder(
    private val initialTiles: List<TileSchema>,
    private val initialEvents: List<EventSchema>,
    private val failureTiles: List<TileSchema>,
    private val failureEvents: List<EventSchema>,
    private val navigationData: Map<String, Any>?,
    private val eventManager: EventManager,
    val tilesManager: TilesManager,
    val tileRendererManager: TileRendererManager
) : ScreenStateHolder<State, Event, Effect>(),
    ScreenBehaviorsHolder,
    DataHolder by DefaultDataHolder(navigationData.orEmpty()) {

    private val internalBroadcastChannel = MutableSharedFlow<BroadcastData>()
    val broadcastChannel get() = internalBroadcastChannel.asSharedFlow()

    override val internalUIState = MutableStateFlow<State>(State.Initial())

    init {
        eventManager.setStateHolderCoroutineScope(stateHolderScope)
        tilesManager.setup(
            tiles = initialTiles,
            events = initialEvents,
            onUpdateStateRequest = ::onUpdateStateRequest
        )
    }

    override fun setState(state: ScreenBehaviorsHolder.State) {
        when (state) {
            ScreenBehaviorsHolder.State.Initial -> {
                internalUIState.update { State.Initial() }
                tilesManager.setup(
                    tiles = initialTiles,
                    events = initialEvents,
                    onUpdateStateRequest = ::onUpdateStateRequest
                )
            }

            ScreenBehaviorsHolder.State.Failure -> {
                internalUIState.update { State.Failure() }
                tilesManager.setup(
                    tiles = failureTiles,
                    events = failureEvents,
                    onUpdateStateRequest = ::onUpdateStateRequest
                )
            }

            is ScreenBehaviorsHolder.State.Success -> {
                internalUIState.update { State.Displaying() }
                tilesManager.setup(
                    tiles = state.screenModel.tiles,
                    navigationDrawerTiles = state.screenModel.navigationDrawerTiles,
                    events = state.screenModel.events,
                    onUpdateStateRequest = ::onUpdateStateRequest
                )
            }
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.OnUIEvent -> onUIEvent(event)
            is Event.OnScreenCoroutineScopeSet -> onScreenCoroutineScopeSet(event.coroutineScope)
        }
    }

    private fun onUIEvent(
        event: Event.OnUIEvent
    ) {
        when (event.event) {
            is UIEvent.TileEventHolderUIEvent ->
                tilesManager.onEvent(
                    tileId = event.event.tileId,
                    event = event.event.event,
                )

            is UIEvent.TileGroupEventHolderUIEvent ->
                tilesManager.onGroupEvent(event.event.event)

            is UIEvent.EventSchemaHolderUIEvent -> {
                eventManager.runEvents(
                    eventSchemas = event.event.events,
                    data = event.event.data
                )
            }
        }
    }

    private fun onScreenCoroutineScopeSet(
        coroutineScope: CoroutineScope
    ) {
        eventManager.setScreenCoroutineScope(coroutineScope)
    }

    private fun onUpdateStateRequest(rootTile: TileSchema) {
        internalUIState.update { currentState ->
            when (currentState) {
                is State.Displaying -> currentState.copy(rootTile = rootTile)
                is State.Failure -> currentState.copy(rootTile = rootTile)
                is State.Initial -> currentState.copy(rootTile = rootTile)
            }
        }
    }

    override fun broadcastData(data: BroadcastData) {
        stateHolderScope.launch {
            internalBroadcastChannel.emit(data)
        }
    }
}