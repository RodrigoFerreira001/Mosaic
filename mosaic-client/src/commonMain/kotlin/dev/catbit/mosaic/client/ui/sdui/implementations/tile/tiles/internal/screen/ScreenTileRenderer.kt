package dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheet
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import dev.catbit.mosaic.client.extensions.observeScreenTileBroadcastChannel
import dev.catbit.mosaic.client.extensions.observeSystemBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.events.OverlayDisplayCallbackHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderingScope
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import kotlin.time.Duration.Companion.seconds
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

object ScreenTileRenderer : TileRenderer<ScreenTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: ScreenTileSchema) {

        LaunchedEffect(tileSchema.state) {
            triggerEvent(
                trigger = EventTriggers.onDisplay()
            )
        }

        LifecycleStartEffect(tileSchema.state) {
            triggerEvent(
                trigger = EventTriggers.onScreenStart()
            )

            onStopOrDispose {
                triggerEvent(
                    trigger = EventTriggers.onScreenStop()
                )
            }
        }

        LifecycleResumeEffect(tileSchema.state) {
            triggerEvent(
                trigger = EventTriggers.onResume()
            )

            onPauseOrDispose {
                triggerEvent(
                    trigger = EventTriggers.onPause()
                )
            }
        }

        with(tileSchema) {

            val coroutineScope = rememberCoroutineScope()
            val navigationDrawerState = rememberDrawerState(DrawerValue.Closed)
            val snackbarHostState = remember { SnackbarHostState() }

            observeScreenTileBroadcastChannel<ScreenTileScreenTilesBroadcastData>(
                filterByTileId = false
            ) { data ->
                when (data) {
                    is ScreenTileScreenTilesBroadcastData.DisplaySnackbar -> coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = data.message,
                            duration = data.duration,
                            actionLabel = data.actionLabel
                        )
                        when (result) {
                            SnackbarResult.Dismissed -> data.onDismiss?.invoke()
                            SnackbarResult.ActionPerformed -> data.onAction?.invoke()
                        }
                    }

                    is ScreenTileScreenTilesBroadcastData.DismissSnackbar -> coroutineScope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }

                    is ScreenTileScreenTilesBroadcastData.DisplayNavigationDrawer -> coroutineScope.launch {
                        navigationDrawerState.open()
                    }

                    is ScreenTileScreenTilesBroadcastData.DismissNavigationDrawer -> coroutineScope.launch {
                        navigationDrawerState.close()
                    }
                }
            }

            observeSystemBroadcastChannel(tileSchema) { data ->
                triggerEvent(
                    trigger = EventTriggers.onSystemBroadcast(data.broadcastId),
                    data = data.data
                )
            }

            ModalNavigationDrawer(
                modifier = Modifier.fillMaxSize(),
                drawerState = navigationDrawerState,
                gesturesEnabled = navigationDrawerTiles != null,
                drawerContent = {
                    navigationDrawerTiles?.let {
                        ModalDrawerSheet {
                            RenderChildren(it)
                        }
                    }
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        RenderChildren(tiles)
                        StackableOverlays(stackableOverlays)
                        SnackbarOverlay(snackbarHostState)
                    }
                }
            }
        }
    }

    @Composable
    private fun TileRenderingScope.StackableOverlays(
        overlays: ImmutableMap<String, ScreenTileSchema.StackableOverlay>,
    ) {
        overlays.forEach { (instanceKey, overlay) ->
            key(instanceKey) {
                when (overlay) {
                    is ScreenTileSchema.StackableOverlay.BottomSheet -> BottomSheetOverlay(
                        instanceKey = instanceKey,
                        overlay = overlay
                    )

                    is ScreenTileSchema.StackableOverlay.ModalBottomSheet -> ModalBottomSheetOverlay(
                        instanceKey = instanceKey,
                        overlay = overlay
                    )

                    is ScreenTileSchema.StackableOverlay.Dialog -> DialogOverlay(
                        instanceKey = instanceKey,
                        overlay = overlay
                    )
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun TileRenderingScope.BottomSheetOverlay(
        instanceKey: String,
        overlay: ScreenTileSchema.StackableOverlay.BottomSheet
    ) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = !overlay.allowsPartialExpansion
        )

        val overlayDisplayCallbackHolder = koinInject<OverlayDisplayCallbackHolder>()

        val finish = {
            overlayDisplayCallbackHolder.cancel(instanceKey)
            dispatchEvent(ScreenTileEvents.OnDismissOverlayFinished(instanceKey))
        }

        LaunchedEffect(instanceKey) {
            overlayDisplayCallbackHolder.fire(instanceKey)
        }

        LaunchedEffect(Unit) {
            sheetState.show()
        }

        LaunchedEffect(overlay.isDismissing) {
            if (overlay.isDismissing) {
                runCatching { sheetState.hide() }
                finish()
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            BottomSheet(
                onDismissRequest = finish,
                state = sheetState,
                maxWidth = 400.dp,
                dragHandle = null,
                gesturesEnabled = overlay.isCancellable,
                backHandlerEnabled = overlay.isCancellable
            ) {
                SheetContent(
                    isCancellable = overlay.isCancellable,
                    fill = overlay.fill
                ) {
                    RenderChildren(overlay.tiles)
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun TileRenderingScope.ModalBottomSheetOverlay(
        instanceKey: String,
        overlay: ScreenTileSchema.StackableOverlay.ModalBottomSheet
    ) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = !overlay.allowsPartialExpansion
        )

        val overlayDisplayCallbackHolder = koinInject<OverlayDisplayCallbackHolder>()

        val finish = {
            overlayDisplayCallbackHolder.cancel(instanceKey)
            dispatchEvent(ScreenTileEvents.OnDismissOverlayFinished(instanceKey))
        }

        LaunchedEffect(instanceKey) {
            overlayDisplayCallbackHolder.fire(instanceKey)
        }

        LaunchedEffect(Unit) {
            sheetState.show()
        }

        LaunchedEffect(overlay.isDismissing) {
            if (overlay.isDismissing) {
                runCatching { sheetState.hide() }
                finish()
            }
        }

        ModalBottomSheet(
            onDismissRequest = finish,
            sheetState = sheetState,
            sheetMaxWidth = 400.dp,
            dragHandle = null,
            sheetGesturesEnabled = overlay.isCancellable,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = overlay.isCancellable,
                shouldDismissOnClickOutside = overlay.isCancellable
            )
        ) {
            SheetContent(
                isCancellable = overlay.isCancellable,
                fill = overlay.fill
            ) {
                RenderChildren(overlay.tiles)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SheetContent(
        isCancellable: Boolean,
        fill: Boolean,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (fill) Modifier.fillMaxHeight() else Modifier)
        ) {
            if (isCancellable) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    BottomSheetDefaults.DragHandle()
                }
            } else {
                Box(modifier = Modifier.size(44.dp))
            }
            content()
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun TileRenderingScope.DialogOverlay(
        instanceKey: String,
        overlay: ScreenTileSchema.StackableOverlay.Dialog
    ) {
        val overlayDisplayCallbackHolder = koinInject<OverlayDisplayCallbackHolder>()

        val finish = {
            overlayDisplayCallbackHolder.cancel(instanceKey)
            dispatchEvent(ScreenTileEvents.OnDismissOverlayFinished(instanceKey))
        }

        LaunchedEffect(instanceKey) {
            overlayDisplayCallbackHolder.fire(instanceKey)
        }

        LaunchedEffect(overlay.isDismissing) {
            if (overlay.isDismissing) finish()
        }

        BasicAlertDialog(
            onDismissRequest = { if (overlay.isCancellable) finish() },
            properties = DialogProperties(
                dismissOnBackPress = overlay.isCancellable,
                dismissOnClickOutside = overlay.isCancellable,
                usePlatformDefaultWidth = overlay.usePlatformDefaultWidth
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    RenderChildren(overlay.tiles)
                }
            }
        }
    }

    @Composable
    private fun SnackbarOverlay(
        hostState: SnackbarHostState,
        bottomPadding: Dp = 24.dp
    ) {
        val snackbarData = hostState.currentSnackbarData
        var isLayerVisible by remember { mutableStateOf(false) }

        LaunchedEffect(snackbarData) {
            if (snackbarData != null) {
                isLayerVisible = true
            } else {
                delay(snackbarLayerGraceMillis)
                isLayerVisible = false
            }
        }

        if (!isLayerVisible) return

        val bottomPaddingPx = with(LocalDensity.current) { bottomPadding.roundToPx() }

        Popup(
            popupPositionProvider = remember(bottomPaddingPx) {
                BottomCenterPositionProvider(bottomPaddingPx)
            },
            properties = PopupProperties(focusable = false)
        ) {
            SnackbarHost(hostState)
        }
    }

    private val snackbarLayerGraceMillis = 0.2.seconds

    private class BottomCenterPositionProvider(
        private val bottomPaddingPx: Int
    ) : PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect,
            windowSize: IntSize,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize
        ) = IntOffset(
            x = (windowSize.width - popupContentSize.width) / 2,
            y = windowSize.height - popupContentSize.height - bottomPaddingPx
        )
    }
}