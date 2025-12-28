package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTileRendererManager
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun MosaicScreen(
    screenId: String,
    navigationData: Map<String, Any>?,
    stateHolder: MosaicScreenStateHolder = koinViewModel {
        parametersOf(screenId, navigationData)
    }
) {

    // TODO Utilizar aqui o conceito de behavior

    /*
    * interface Behavior {
    *   @Composable
    *   fun execute() {
    *
    *   }
    * */

    //

    stateHolder.bindScreenLifecycle()

    val uiState by stateHolder.uiState.collectAsState()

    CompositionLocalProvider(
        LocalTileRendererManager provides stateHolder.tileRendererManager
    ) {
        ControlHomeScreenContent(
            uiState = uiState,
            onEvent = { stateHolder.onEvent(it) }
        )
    }
}

@Composable
private fun ControlHomeScreenContent(
    uiState: State,
    onEvent: (Event) -> Unit
) {
    when (uiState) {
        is State.Displaying -> MosaicScreenDisplayingContent(uiState, onEvent)
        is State.Failure -> MosaicScreenFailureContent(uiState, onEvent)
        is State.Loading -> MosaicScreenLoadingContent()
    }
}

@Composable
private fun MosaicScreenDisplayingContent(
    uiState: State.Displaying,
    onEvent: (Event) -> Unit
) {
    uiState.tiles.forEach { state ->
        LocalTileRendererManager.current.Render(
            uiState = state,
            onEvent = { onEvent(Event.OnUIEvent(it)) }
        )
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