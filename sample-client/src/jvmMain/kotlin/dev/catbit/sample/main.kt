package dev.catbit.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.catbit.mosaic.sample.Sample

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(placement = WindowPlacement.Maximized)
    ) {
        Sample()
    }
}