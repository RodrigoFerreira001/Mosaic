package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.domain.GetScreenUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.base.ScreenStateHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRendererManager
import dev.catbit.mosaic.core.data.tile.TileModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MosaicScreenStateHolder(
    private val screenId: String,
    private val navigationData: Map<String, Any>?,
    private val getScreenUseCase: GetScreenUseCase,
    private val tilesUIStateManager: TilesManager,
    private val eventManager: EventManager,
    val tileRendererManager: TileRendererManager
) : ScreenStateHolder<State, Event, Effect>(),
    ScreenBehaviorsHolder,
    DataHolder by DefaultDataHolder() {

    private val internalBroadcastChannel = MutableSharedFlow<BroadcastData>()
    val broadcastChannel get() = internalBroadcastChannel.asSharedFlow()

    override val internalUIState = MutableStateFlow<State>(State.Loading)

    override fun onFirstDisplay() {
        fetchScreen()
    }

    private fun fetchScreen() {
        stateHolderScope.launch {
            internalUIState.update { State.Loading }
            getScreenUseCase(
                GetScreenUseCase.Params(screenId, navigationData)
            ).fold(
                onFailure = {
                    internalUIState.update { State.Failure }
                },
                onSuccess = { screen ->
                    tilesUIStateManager.setup(
                        tiles = screen.tiles,
                        navigationDrawerTiles = screen.navigationDrawerTiles,
                        events = screen.events,
                        onUpdateStateRequest = ::onUpdateStateRequest
                    )
                }
            )
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.OnUIEvent -> onUIEvent(event)
            Event.OnTryAgainClick -> onTryAgainClick()
        }
    }

    private fun onUIEvent(event: Event.OnUIEvent) {
        when (event.event) {
            is UIEvent.TileEventHolderUIEvent -> tilesUIStateManager.onEvent(
                tileId = event.event.tileId,
                event = event.event.event,
            )

            is UIEvent.EventModelHolderUIEvent -> {
                stateHolderScope.launch {
                    eventManager.runEvents(
                        eventModels = event.event.events,
                        data = event.event.data
                    )
                }
            }
        }
    }

    private fun onUpdateStateRequest(rootTile: TileModel) {
        internalUIState.update { currentState ->
            when (currentState) {
                State.Failure, State.Loading -> State.Displaying(rootTile)
                is State.Displaying -> {
                    currentState.copy(rootTile = rootTile)
                }
            }
        }
    }

    private fun onTryAgainClick() {
        fetchScreen()
    }

    override fun refresh() {
        fetchScreen()
    }

    override fun broadcastData(data: BroadcastData) {
        stateHolderScope.launch {
            internalBroadcastChannel.emit(data)
        }
    }
}