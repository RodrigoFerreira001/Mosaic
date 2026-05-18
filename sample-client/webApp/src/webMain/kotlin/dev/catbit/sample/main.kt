package dev.catbit.sample

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.catbit.mosaic.client.ui.effects.SingleEffect
import dev.catbit.mosaic.sample.App
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.getElementById("mainContent")!!) {
        App()
        SingleEffect {
            document.getElementById("loadingContent")?.remove()
        }
    }
}