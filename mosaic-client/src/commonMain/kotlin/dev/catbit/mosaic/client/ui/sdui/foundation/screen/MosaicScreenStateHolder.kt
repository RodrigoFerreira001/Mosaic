package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.domain.GetScreenUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastData
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.base.ScreenStateHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesUIStateManager
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRendererManager
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.bottom_sheet.BottomSheetTileModel
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.bottom_sheet.BottomSheetTileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.dialog.DialogTileModel
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.dialog.DialogTileUIState
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.navigation_drawer.NavigationDrawerTileModel
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.internal.navigation_drawer.NavigationDrawerTileUIState
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
    private val tilesUIStateManager: TilesUIStateManager,
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
                    screen.events?.let { events ->
                        eventManager.registerEvents(
                            eventOwnerId = screenId,
                            eventList = events
                        )
                    }
                    tilesUIStateManager.setup(
                        tiles = screen.tiles.toMutableList().apply {
                            screen.navigationDrawer?.let { navigationDrawerTiles ->
                                add(
                                    NavigationDrawerTileModel(
                                        id = "MOSAIC_NAVIGATION_DRAWER",
                                        tiles = navigationDrawerTiles
                                    )
                                )
                            }
                        },
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
            Event.OnCloseBottomSheetFinished -> onCloseBottomSheetFinished()
            Event.OnCloseDialogFinished -> onCloseDialogFinished()
        }
    }

    private fun onUIEvent(event: Event.OnUIEvent) {
        when (event.event) {
            is UIEvent.TileEventHolderUIEvent -> tilesUIStateManager.onEvent(
                tileId = event.event.tileId,
                event = event.event.event,
            )

            is UIEvent.TriggerHolderUIEvent -> {
                stateHolderScope.launch {
                    eventManager.triggerEvent(
                        eventOwnerId = event.event.eventOwnerId,
                        trigger = event.event.trigger,
                        data = event.event.data
                    )
                }
            }
        }
    }

    private fun onUpdateStateRequest(tiles: List<TileUIState>) {
        internalUIState.update { currentState ->
            when (currentState) {
                State.Failure, State.Loading -> State.Displaying(tiles)
                is State.Displaying -> {

                    val bottomSheetTile = tiles.firstOrNull { it.id == "MOSAIC_BOTTOM_SHEET" }
                    val dialogTile = tiles.firstOrNull { it.id == "MOSAIC_DIALOG" }
                    val navigationDrawerTile = tiles.firstOrNull { it.id == "MOSAIC_NAVIGATION_DRAWER" }

                    currentState.copy(
                        screenTiles = tiles.filter {
                            it != bottomSheetTile
                                    && it != dialogTile
                                    && it != navigationDrawerTile
                        },
                        bottomSheetUIState = (bottomSheetTile as? BottomSheetTileUIState)?.let {
                            currentState.bottomSheetUIState?.copy(
                                tiles = bottomSheetTile.tiles
                            ) ?: State.Displaying.BottomSheetUIState(
                                tiles = bottomSheetTile.tiles,
                            )
                        },
                        dialogUIState = (dialogTile as? DialogTileUIState)?.let {
                            currentState.dialogUIState?.copy(
                                tiles = dialogTile.tiles
                            ) ?: State.Displaying.DialogUIState(
                                tiles = dialogTile.tiles
                            )
                        },
                        navigationDrawerUIState = (navigationDrawerTile as? NavigationDrawerTileUIState)?.let {
                            currentState.navigationDrawerUIState?.copy(
                                tiles = navigationDrawerTile.tiles
                            ) ?: State.Displaying.NavigationDrawerUIState(
                                tiles = navigationDrawerTile.tiles,
                            )
                        }
                    )
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

    override fun displayDialog(
        isCancellable: Boolean,
        usePlatformDefaultWidth: Boolean,
        tiles: List<TileModel>
    ) {
        tilesUIStateManager.addTile(
            DialogTileModel(
                id = "MOSAIC_DIALOG",
                tiles = tiles
            )
        )
        internalEffects.dispatch(
            Effect.OnDisplayDialogRequested(
                isCancellable = isCancellable,
                usePlatformDefaultWidth = usePlatformDefaultWidth,
            )
        )
    }

    override fun dismissDialog() {
        internalEffects.dispatch(Effect.OnCloseDialogRequested)
    }

    fun onCloseDialogFinished() {
        tilesUIStateManager.removeTile("MOSAIC_DIALOG")
    }

    override fun displayBottomSheet(
        isCancellable: Boolean,
        fill: Boolean,
        tiles: List<TileModel>
    ) {
        tilesUIStateManager.addTile(
            BottomSheetTileModel(
                id = "MOSAIC_BOTTOM_SHEET",
                tiles = tiles
            )
        )
        internalEffects.dispatch(
            Effect.OnDisplayBottomSheetRequested(
                isCancellable = isCancellable,
                fill = fill
            )
        )
    }

    override fun dismissBottomSheet() {
        internalEffects.dispatch(Effect.OnCloseBottomSheetRequested)
    }

    private fun onCloseBottomSheetFinished() {
        tilesUIStateManager.removeTile("MOSAIC_BOTTOM_SHEET")
    }

    override fun displaySnackbar(
        message: String
    ) {
        internalEffects.dispatch(
            Effect.OnDisplaySnackbar(
                message = message
            )
        )
    }

    override fun closeSnackbar() {
        internalEffects.dispatch(Effect.OnCloseSnackbarRequested)
    }

    override fun displayNavigationDrawer() {
        internalEffects.dispatch(Effect.OnDisplayNavigationDrawerSheetRequested)
    }

    override fun dismissNavigationDrawer() {
        internalEffects.dispatch(Effect.OnCloseNavigationDrawerSheetRequested)
    }

    override fun broadcastData(data: BroadcastData) {
        stateHolderScope.launch {
            internalBroadcastChannel.emit(data)
        }
    }
}