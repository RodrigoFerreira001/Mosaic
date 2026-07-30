package dev.catbit.mosaic.client.data.data_sources.system

import kotlinx.browser.window

internal actual suspend fun openExternalLink(url: String) {
    window.open(url, "_blank")
}
