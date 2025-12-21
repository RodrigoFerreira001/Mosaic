package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.domain.GetScreenUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.base.ScreenStateHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesUIStateManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRendererManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MosaicScreenStateHolder(
    private val screenId: String,
    private val navigationData: Map<String, Any>?,
    private val getScreenUseCase: GetScreenUseCase,
    private val tilesUIStateManager: TilesUIStateManager,
    private val eventManager: EventManager,
    val tileRendererManager: TileRendererManager
) : ScreenStateHolder<State, Event, Effect>() {

    override val internalUIState = MutableStateFlow<State>(State.Loading)

    override fun onFirstDisplay() {
        stateHolderScope.launch {
            flow {
                getScreenUseCase(
                    GetScreenUseCase.Params(screenId, navigationData)
                ).fold(
                    onFailure = {
                        emit(State.Failure)
                    },
                    onSuccess = { screen ->
                        screen.events?.let { events ->
                            eventManager.registerEvents(
                                eventOwnerId = screenId,
                                eventList = events
                            )
                        }
                        tilesUIStateManager.setup(screen.tiles)
                        emitAll(
                            tilesUIStateManager.uiState.map { tiles ->
                                State.Displaying(tiles)
                            }
                        )
                    }
                )
            }.collectLatest { newState ->
                internalUIState.update { newState }
            }
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

            is UIEvent.TriggerHolderUIEvent -> eventManager.triggerEvent(
                eventOwnerId = event.event.eventOwnerId,
                trigger = event.event.trigger
            )
        }
    }

    private fun onTryAgainClick() {

    }
}