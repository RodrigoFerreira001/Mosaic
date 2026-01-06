package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import dev.catbit.mosaic.client.extensions.consume
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTileRendererManager
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

    CompositionLocalProvider(
        LocalTileRendererManager provides stateHolder.tileRendererManager,
        LocalBroadcastChannel provides stateHolder.broadcastChannel
    ) {
        ControlHomeScreenContent(
            uiState = uiState,
            uiEffects = stateHolder.effects,
            onEvent = { stateHolder.onEvent(it) }
        )
    }
}

@Composable
private fun ControlHomeScreenContent(
    uiState: State,
    uiEffects: SharedFlow<Effect>,
    onEvent: (Event) -> Unit
) {
    when (uiState) {
        is State.Displaying -> MosaicScreenDisplayingContent(uiState, uiEffects, onEvent)
        is State.Failure -> MosaicScreenFailureContent(uiState, onEvent)
        is State.Loading -> MosaicScreenLoadingContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MosaicScreenDisplayingContent(
    uiState: State.Displaying,
    uiEffects: SharedFlow<Effect>,
    onEvent: (Event) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }

    uiEffects.consume { effect ->
        when (effect) {
            is Effect.OnDisplaySnackbar -> {
                when (snackbarHostState.showSnackbar(effect.message)) {
                    SnackbarResult.Dismissed -> println("Snackbar dismissed")
                    SnackbarResult.ActionPerformed -> println("Snackbar action performed")
                }
            }

            Effect.OnCloseBottomSheetRequested -> sheetState.hide()
            Effect.OnCloseNavigationDrawerSheetRequested -> drawerState.close()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        ModalNavigationDrawer(
            modifier = Modifier.fillMaxSize(),
            drawerContent = {
                uiState.navigationDrawerUIState?.tiles?.forEach { state ->
                    LocalTileRendererManager.current.Render(
                        uiState = state,
                        onEvent = { onEvent(Event.OnUIEvent(it)) }
                    )
                }
            },
            drawerState = drawerState,
            gesturesEnabled = true
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) {
                uiState.screenTiles.forEach { state ->
                    LocalTileRendererManager.current.Render(
                        uiState = state,
                        onEvent = { onEvent(Event.OnUIEvent(it)) }
                    )
                }
            }
        }

        uiState.bottomSheetUIState?.let { bottomSheetUIState ->
            ModalBottomSheet(
                onDismissRequest = {
                    onEvent(Event.OnCloseBottomSheetFinished)
                },
                sheetState = sheetState,
                properties = ModalBottomSheetProperties(
                    shouldDismissOnBackPress = bottomSheetUIState.isCancellable,
                    shouldDismissOnClickOutside = bottomSheetUIState.isCancellable
                ),
                content = {
                    bottomSheetUIState.tiles.forEach { state ->
                        LocalTileRendererManager.current.Render(
                            uiState = state,
                            onEvent = { onEvent(Event.OnUIEvent(it)) }
                        )
                    }
                }
            )
        }

        uiState.dialogUIState?.let { dialogUIState ->
            BasicAlertDialog(
                onDismissRequest = {
                    onEvent(Event.OnCloseDialogRequested)
                },
                properties = DialogProperties(
                    dismissOnBackPress = dialogUIState.isCancellable,
                    dismissOnClickOutside = dialogUIState.isCancellable,
                    usePlatformDefaultWidth = dialogUIState.usePlatformDefaultWidth
                ),
                content = {
                    dialogUIState.tiles.forEach { state ->
                        LocalTileRendererManager.current.Render(
                            uiState = state,
                            onEvent = { onEvent(Event.OnUIEvent(it)) }
                        )
                    }
                }
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