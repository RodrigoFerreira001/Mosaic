package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import dev.catbit.mosaic.client.extensions.observeScreenTileBroadcastChannel
import dev.catbit.mosaic.client.extensions.observeSystemBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalScreenTilesBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTileRendererManager
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.basic_dialog.LocalDialogState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.bottom_sheet.LocalBottomSheetState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.navigation_drawer.LocalNavigationDrawerState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.snackbar.LocalSnackBarState
import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRendererManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.collections.immutable.ImmutableList

object ScreenTileRenderer : TileRenderer<ScreenTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: ScreenTileSchema) {

        LaunchedEffect(tileSchema.state) {
            triggerEvent(
                trigger = EventTriggers.onDisplay()
            )
        }

        with(tileSchema) {

            val snackbarState = LocalSnackBarState.current
            val bottomSheetState = LocalBottomSheetState.current
            val dialogState = LocalDialogState.current
            val navigationDrawerState = LocalNavigationDrawerState.current
            val broadcastChannel = LocalScreenTilesBroadcastChannel.current
            val tileRendererManager = LocalTileRendererManager.current

            observeScreenTileBroadcastChannel<ScreenTileScreenTilesBroadcastData>(
                filterByTileId = false
            ) { data ->
                when (data) {
                    is ScreenTileScreenTilesBroadcastData.DismissBottomSheet -> bottomSheetState.dismiss()
                    is ScreenTileScreenTilesBroadcastData.DismissDialog -> dialogState.dismiss()
                    is ScreenTileScreenTilesBroadcastData.DisplaySnackbar -> snackbarState.show(
                        message = data.message,
                        duration = data.duration,
                        actionLabel = data.actionLabel,
                        onAction = data.onAction,
                        onDismiss = data.onDismiss
                    )

                    is ScreenTileScreenTilesBroadcastData.DismissSnackbar -> snackbarState.dismiss()

                    is ScreenTileScreenTilesBroadcastData.OnDisplayBottomSheetRequested -> bottomSheetState.show( // TODO Analisar como será o dismiss por swipedown
                        fill = data.fill,
                        cancellable = data.isCancellable,
                        onDismiss = { dispatchEvent(ScreenTileEvents.OnCloseBottomSheetFinished) }
                    )

                    is ScreenTileScreenTilesBroadcastData.OnDisplayDialogRequested -> dialogState.show(
                        cancellable = data.isCancellable,
                        constrainInPlatformWidth = data.usePlatformDefaultWidth,
                        onDismiss = { dispatchEvent(ScreenTileEvents.OnCloseDialogFinished) }
                    )

                    is ScreenTileScreenTilesBroadcastData.DismissNavigationDrawer -> navigationDrawerState.dismiss()
                    is ScreenTileScreenTilesBroadcastData.DisplayNavigationDrawer -> {
                        navigationDrawerTiles?.let {
                            navigationDrawerState.show(
                                onDismiss = { navigationDrawerState.dismiss() }
                            )
                        }
                    }
                }
            }

            observeSystemBroadcastChannel { data ->
                triggerEvent(
                    trigger = EventTriggers.onSystemBroadcastEventTrigger(data.broadcastId),
                    data = data.data
                )
            }

            LaunchedEffect(currentBottomSheetTiles) {
                currentBottomSheetTiles?.let {
                    bottomSheetState.updateContent {
                        BaseTileScope(
                            tiles = currentBottomSheetTiles,
                            screenTilesBroadcastChannel = broadcastChannel,
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
                            screenTilesBroadcastChannel = broadcastChannel,
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
                            screenTilesBroadcastChannel = broadcastChannel,
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
        tiles: ImmutableList<TileSchema>,
        screenTilesBroadcastChannel: ScreenTilesBroadcastChannel,
        tileRendererManager: TileRendererManager,
        onEvent: (UIEvent) -> Unit
    ) {
        CompositionLocalProvider(
            LocalTileRendererManager provides tileRendererManager,
            LocalScreenTilesBroadcastChannel provides screenTilesBroadcastChannel
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