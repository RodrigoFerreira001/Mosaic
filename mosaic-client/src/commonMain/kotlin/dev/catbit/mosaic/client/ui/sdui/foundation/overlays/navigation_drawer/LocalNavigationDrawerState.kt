package dev.catbit.mosaic.client.ui.sdui.foundation.overlays.navigation_drawer

import androidx.compose.runtime.compositionLocalOf

val LocalNavigationDrawerState = compositionLocalOf<NavigationDrawerState> {
    error("NavigationDrawerState was not provided")
}