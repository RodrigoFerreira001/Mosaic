package dev.catbit.mosaic.client.data.data_sources.system

import java.awt.Desktop
import java.net.URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal actual suspend fun openExternalLink(url: String) {
    withContext(Dispatchers.IO) {
        Desktop.getDesktop().browse(URI(url))
    }
}
