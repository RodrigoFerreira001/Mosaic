package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.extensions.consume
import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTileRendererManager
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.basic_dialog.LocalDialogState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.bottom_sheet.LocalBottomSheetState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.navigation_drawer.LocalNavigationDrawerState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.snackbar.LocalSnackBarState
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRendererManager
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MosaicScreen(
    screenId: String,
    navigationData: Map<String, Any>?,
    stateHolder: MosaicScreenStateHolder = koinViewModel(
        key = screenId
    ) {
        parametersOf(screenId, navigationData)
    }
) {
    stateHolder.bindScreenLifecycle()

    val uiState by stateHolder.uiState.collectAsState()

    ControlHomeScreenContent(
        uiState = uiState,
        uiEffects = stateHolder.effects,
        broadcastChannel = stateHolder.broadcastChannel,
        tileRendererManager = stateHolder.tileRendererManager,
        onEvent = { stateHolder.onEvent(it) }
    )
}

@Composable
private fun ControlHomeScreenContent(
    uiState: State,
    uiEffects: SharedFlow<Effect>,
    broadcastChannel: BroadcastChannel,
    tileRendererManager: TileRendererManager,
    onEvent: (Event) -> Unit
) {
    when (uiState) {
        is State.Displaying -> MosaicScreenDisplayingContent(
            uiState,
            uiEffects,
            broadcastChannel,
            tileRendererManager,
            onEvent
        )

        is State.Failure -> MosaicScreenFailureContent(uiState, onEvent)
        is State.Loading -> MosaicScreenLoadingContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MosaicScreenDisplayingContent(
    uiState: State.Displaying,
    uiEffects: SharedFlow<Effect>,
    broadcastChannel: BroadcastChannel,
    tileRendererManager: TileRendererManager,
    onEvent: (Event) -> Unit
) {

    CompositionLocalProvider(
        LocalTileRendererManager provides tileRendererManager,
        LocalBroadcastChannel provides broadcastChannel
    ) {
        val snackbarState = LocalSnackBarState.current
        val bottomSheetState = LocalBottomSheetState.current
        val dialogState = LocalDialogState.current
        val navigationDrawerState = LocalNavigationDrawerState.current

        uiEffects.consume { effect ->
            when (effect) {
                is Effect.OnDisplaySnackbar -> snackbarState.show(effect.message) // TODO completar como demais propriedades

                is Effect.OnDisplayBottomSheetRequested -> bottomSheetState.show( // TODO Analisar como será o dismiss por swipedown
                    fill = effect.fill,
                    cancellable = effect.isCancellable,
                    onDismiss = { onEvent(Event.OnCloseBottomSheetFinished) }
                )

                Effect.OnCloseBottomSheetRequested -> bottomSheetState.dismiss()

                is Effect.OnDisplayDialogRequested -> dialogState.show(
                    cancellable = effect.isCancellable,
                    onDismiss = { onEvent(Event.OnCloseDialogFinished) },
                    constrainInPlatformWidth = effect.usePlatformDefaultWidth
                )

                Effect.OnCloseDialogRequested -> dialogState.dismiss()

                Effect.OnDisplayNavigationDrawerSheetRequested -> {
                    uiState.navigationDrawerUIState?.let {
                        navigationDrawerState.show(
                            onDismiss = { navigationDrawerState.dismiss() }
                        )
                    }
                }

                Effect.OnCloseNavigationDrawerSheetRequested -> navigationDrawerState.dismiss()
                Effect.OnCloseSnackbarRequested -> snackbarState.dismiss()
            }
        }

        LaunchedEffect(uiState.bottomSheetUIState) {
            uiState.bottomSheetUIState?.let {
                bottomSheetState.updateContent {
                    BaseTileScope(
                        tiles = uiState.bottomSheetUIState.tiles,
                        broadcastChannel = broadcastChannel,
                        tileRendererManager = tileRendererManager,
                        onEvent = onEvent,
                    )
                }
            }
        }

        LaunchedEffect(uiState.navigationDrawerUIState) {
            uiState.navigationDrawerUIState?.let {
                navigationDrawerState.updateContent {
                    BaseTileScope(
                        tiles = uiState.navigationDrawerUIState.tiles,
                        broadcastChannel = broadcastChannel,
                        tileRendererManager = tileRendererManager,
                        onEvent = onEvent,
                    )
                }
            }
        }

        LaunchedEffect(uiState.dialogUIState) {
            uiState.dialogUIState?.let {
                dialogState.updateContent {
                    BaseTileScope(
                        tiles = uiState.dialogUIState.tiles,
                        broadcastChannel = broadcastChannel,
                        tileRendererManager = tileRendererManager,
                        onEvent = onEvent,
                    )
                }
            }
        }

        uiState.screenTiles.forEach { state ->
            tileRendererManager.Render(
                uiState = state,
                onEvent = { onEvent(Event.OnUIEvent(it)) }
            )
        }
    }
}

@Composable
fun MosaicScreenFailureContent(
    uiState: State.Failure,
    onEvent: (Event) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        Text("Error")
        Button(
            onClick = { onEvent(Event.OnTryAgainClick) }
        ) {
            Text("Try again")
        }
    }
}

@Composable
fun MosaicScreenLoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun BaseTileScope(
    tiles: List<TileUIState>,
    broadcastChannel: BroadcastChannel,
    tileRendererManager: TileRendererManager,
    onEvent: (Event) -> Unit
) {
    CompositionLocalProvider(
        LocalTileRendererManager provides tileRendererManager,
        LocalBroadcastChannel provides broadcastChannel
    ) {
        tiles.forEach { state ->
            tileRendererManager.Render(
                uiState = state,
                onEvent = { onEvent(Event.OnUIEvent(it)) }
            )
        }
    }
}