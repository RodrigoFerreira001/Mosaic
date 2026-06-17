package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.ScreenNavKey
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalScreenTilesBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTileRendererManager
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTilesManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.TilesManager
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MosaicScreen(
    screenId: String,
    navigationData: ScreenNavKey.NavigationData,
    parent: TilesManager?,
    stateHolder: MosaicScreenStateHolder = koinViewModel(
        key = screenId
    ) {
        parametersOf(screenId, navigationData, parent)
    }
) {
    stateHolder.bindScreenLifecycle()

    val uiState by stateHolder.uiState.collectAsState()

    CompositionLocalProvider(
        LocalTileRendererManager provides stateHolder.tileRendererManager,
        LocalScreenTilesBroadcastChannel provides stateHolder.screenBroadcastChannel,
        LocalTilesManager provides stateHolder.tilesManager
    ) {
        uiState.rootTile?.let { rootTile ->
            LocalTileRendererManager.current.Render(
                tileSchema = rootTile,
                onEvent = { stateHolder.onEvent(Event.OnUIEvent(it)) }
            )
        }
    }
}