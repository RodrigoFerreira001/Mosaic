package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTileRendererManager
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTilesManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.TilesManager
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MosaicScreen(
    screenId: String,
    navigationData: Map<String, Any>?,
    parent: TilesManager?,
    stateHolder: MosaicScreenStateHolder = koinViewModel(
        key = screenId
    ) {
        parametersOf(screenId, navigationData, parent)
    }
) {
    stateHolder.bindScreenLifecycle()

    val coroutineScope = rememberCoroutineScope()
    val uiState by stateHolder.uiState.collectAsState()

    SingleEffect {
        stateHolder.onEvent(Event.OnScreenCoroutineScopeSet(coroutineScope))
    }

    CompositionLocalProvider(
        LocalTileRendererManager provides stateHolder.tileRendererManager,
        LocalBroadcastChannel provides stateHolder.broadcastChannel,
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