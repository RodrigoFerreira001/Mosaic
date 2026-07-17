package dev.catbit.sample.client

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.catbit.mosaic.sample.client.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
    ) {
        App()
    }
}