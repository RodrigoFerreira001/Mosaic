package dev.catbit.mosaic.client.ui.sdui.foundation.overlays

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.basic_dialog.LocalDialogState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.basic_dialog.rememberDialogState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.bottom_sheet.LocalBottomSheetState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.bottom_sheet.rememberBottomSheetState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.navigation_drawer.LocalNavigationDrawerState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.navigation_drawer.rememberNavigationDrawerState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.snackbar.LocalSnackBarState
import dev.catbit.mosaic.client.ui.sdui.foundation.overlays.snackbar.rememberSnackBarState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun OverlayContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val containerCoroutineScope = rememberCoroutineScope()

    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val bottomSheetState = rememberBottomSheetState(
        modalBottomSheetState = modalBottomSheetState,
        coroutineScope = containerCoroutineScope,
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarState = rememberSnackBarState(
        snackbarHostState = snackbarHostState,
        coroutineScope = containerCoroutineScope,
    )

    val dialogState = rememberDialogState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navigationDrawerState = rememberNavigationDrawerState(
        drawerState = drawerState,
        coroutineScope = containerCoroutineScope
    )

    LaunchedEffect(drawerState.currentValue) {
        if (drawerState.isClosed) {
            navigationDrawerState.onDismissCallback?.invoke()
        }
    }

    CompositionLocalProvider(
        LocalBottomSheetState provides bottomSheetState,
        LocalSnackBarState provides snackbarState,
        LocalDialogState provides dialogState,
        LocalNavigationDrawerState provides navigationDrawerState
    ) {
        Box(
            modifier = modifier,
        ) {

            ModalNavigationDrawer(
                modifier = Modifier.fillMaxSize(),
                drawerContent = navigationDrawerState.content,
                drawerState = navigationDrawerState.drawerState,
                gesturesEnabled = true
            ) {
                //Usar um BottomSheetScaffold para o comportamento de NonModalBottomSheet
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarState.snackbarHostState) }
                ) {
                    content()
                }
            }

            if (bottomSheetState.isVisible) {
                ModalBottomSheet(
                    onDismissRequest = bottomSheetState::onDismissRequest,
                    sheetState = bottomSheetState.modalBottomSheetState,
                    sheetMaxWidth = 400.dp,
                    content = bottomSheetState.content,
                    properties = ModalBottomSheetProperties(
                        shouldDismissOnBackPress = bottomSheetState.isCancellable,
                        shouldDismissOnClickOutside = bottomSheetState.isCancellable
                    )
                )
            }

            if (dialogState.isVisible) {
                BasicAlertDialog(
                    modifier = Modifier.widthIn(400.dp),
                    onDismissRequest = {
                        dialogState.onDismissCallback?.invoke()
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = dialogState.isCancellable,
                        dismissOnClickOutside = dialogState.isCancellable,
                        usePlatformDefaultWidth = dialogState.usePlatformDefaultWidth
                    ),
                    content = {
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
                                dialogState.content()
                            }
                        }
                    }
                )
            }
        }
    }
}