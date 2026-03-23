package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import dev.catbit.mosaic.client.extensions.observeBroadcastChannel
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import dev.catbit.mosaic.client.ui.sdui.foundation.broadcast.BroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTileRendererManager
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.basic_dialog.LocalDialogState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.bottom_sheet.LocalBottomSheetState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.navigation_drawer.LocalNavigationDrawerState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.snackbar.LocalSnackBarState
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRendererManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

object ScreenTileRenderer : TileRenderer<ScreenTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: ScreenTileSchema) {

        SingleEffect {
            triggerEvent(
                trigger = EventTriggers.onDisplay()
            )
        }

        with(tileSchema) {

            val snackbarState = LocalSnackBarState.current
            val bottomSheetState = LocalBottomSheetState.current
            val dialogState = LocalDialogState.current
            val navigationDrawerState = LocalNavigationDrawerState.current
            val broadcastChannel = LocalBroadcastChannel.current
            val tileRendererManager = LocalTileRendererManager.current

            observeBroadcastChannel<ScreenTileBroadcastData>(
                filterByTileId = false
            ) { data ->
                when (data) {
                    is ScreenTileBroadcastData.DismissBottomSheet -> bottomSheetState.dismiss()
                    is ScreenTileBroadcastData.DismissDialog -> dialogState.dismiss()
                    is ScreenTileBroadcastData.DisplaySnackbar -> snackbarState.show(
                        message = data.message
                    )

                    is ScreenTileBroadcastData.OnDisplayBottomSheetRequested -> bottomSheetState.show( // TODO Analisar como será o dismiss por swipedown
                        fill = data.fill,
                        cancellable = data.isCancellable,
                        onDismiss = { dispatchEvent(ScreenTileEvents.OnCloseBottomSheetFinished) }
                    )

                    is ScreenTileBroadcastData.OnDisplayDialogRequested -> dialogState.show(
                        cancellable = data.isCancellable,
                        constrainInPlatformWidth = data.usePlatformDefaultWidth,
                        onDismiss = { dispatchEvent(ScreenTileEvents.OnCloseDialogFinished) }
                    )

                    is ScreenTileBroadcastData.DismissNavigationDrawer -> navigationDrawerState.dismiss()
                    is ScreenTileBroadcastData.DisplayNavigationDrawer -> {
                        navigationDrawerTiles?.let {
                            navigationDrawerState.show(
                                onDismiss = { navigationDrawerState.dismiss() }
                            )
                        }
                    }
                }
            }

            LaunchedEffect(currentBottomSheetTiles) {
                currentBottomSheetTiles?.let {
                    bottomSheetState.updateContent {
                        BaseTileScope(
                            tiles = currentBottomSheetTiles,
                            broadcastChannel = broadcastChannel,
                            tileRendererManager = tileRendererManager,
                            onEvent = onEvent,
                        )
                    }
                }
            }

            LaunchedEffect(currentDialogSheetTiles) {
                currentDialogSheetTiles?.let {
                    dialogState.updateContent {
                        BaseTileScope(
                            tiles = currentDialogSheetTiles,
                            broadcastChannel = broadcastChannel,
                            tileRendererManager = tileRendererManager,
                            onEvent = onEvent,
                        )
                    }
                }
            }

            LaunchedEffect(navigationDrawerTiles) {
                navigationDrawerTiles?.let {
                    navigationDrawerState.updateContent {
                        BaseTileScope(
                            tiles = navigationDrawerTiles,
                            broadcastChannel = broadcastChannel,
                            tileRendererManager = tileRendererManager,
                            onEvent = onEvent,
                        )
                    }
                }
            }

            RenderChildren(tiles)
        }
    }

    @Composable
    private fun BaseTileScope(
        tiles: List<TileSchema>,
        broadcastChannel: BroadcastChannel,
        tileRendererManager: TileRendererManager,
        onEvent: (UIEvent) -> Unit
    ) {
        CompositionLocalProvider(
            LocalTileRendererManager provides tileRendererManager,
            LocalBroadcastChannel provides broadcastChannel
        ) {
            tiles.forEach { state ->
                tileRendererManager.Render(
                    tileSchema = state,
                    onEvent = onEvent
                )
            }
        }
    }
}