package dev.catbit.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.catbit.mosaic.sample.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
    ) {
        App()
    }
}