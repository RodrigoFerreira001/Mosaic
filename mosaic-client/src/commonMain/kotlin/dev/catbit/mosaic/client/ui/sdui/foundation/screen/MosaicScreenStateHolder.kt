package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.domain.GetScreenUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.base.ScreenStateHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesUIStateManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRendererManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
) : ScreenStateHolder<State, Event, Effect>(), ScreenBehaviors {

    // TODO introduzir o sistema de brodcast
    // que será responsável por coisas como um evento dizer para uma column fazer scroll

    private val formData: Map<String, Any> = mutableMapOf()
    private val uiData: Map<String, Any> = mutableMapOf()

    private val internalBroadcastChannel = MutableSharedFlow<BroadcastData>()
    val broadcastChannel get() = internalBroadcastChannel.asSharedFlow()

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
                trigger = event.event.trigger,
                data = event.event.data
            )
        }
    }

    private fun onTryAgainClick() {

    }

    override fun refresh() {
        TODO("Not yet implemented")
    }

    override fun navigate() {
        TODO("Not yet implemented")
    }

    override fun goBack() {
        TODO("Not yet implemented")
    }

    override fun displayDialog() {
        TODO("Not yet implemented")
    }

    override fun closeDialog() {
        TODO("Not yet implemented")
    }

    override fun displayBottomSheet() {
        TODO("Not yet implemented")
    }

    override fun closeBottomSheet() {
        TODO("Not yet implemented")
    }

    override fun displaySnackbar() {
        TODO("Not yet implemented")
    }

    override fun closeSnackbar() {
        TODO("Not yet implemented")
    }

    override fun displayDrawer() {
        TODO("Not yet implemented")
    }

    override fun closeDrawer() {
        TODO("Not yet implemented")
    }

    override fun displayMenu() {
        TODO("Not yet implemented")
    }

    override fun closeMenu() {
        TODO("Not yet implemented")
    }

    override fun requestPermission() {
        TODO("Not yet implemented")
    }

    override fun sendNotification() {
        TODO("Not yet implemented")
    }

    override fun getData() {
        TODO("Not yet implemented")
    }

    override fun setData() {
        TODO("Not yet implemented")
    }

    override fun removeData() {
        TODO("Not yet implemented")
    }

    override fun broadcastData(data: BroadcastData) {
        stateHolderScope.launch {
            internalBroadcastChannel.emit(data)
        }
    }
}