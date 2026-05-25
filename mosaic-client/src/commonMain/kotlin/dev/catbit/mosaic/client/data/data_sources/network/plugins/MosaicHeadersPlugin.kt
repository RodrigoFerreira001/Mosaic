package dev.catbit.mosaic.client.data.data_sources.network.plugins

import dev.catbit.mosaic.client.platform.Platform
import io.ktor.client.plugins.api.createClientPlugin

val MosaicHeadersPlugin = createClientPlugin("MosaicHeadersPlugin") {
    onRequest { request, _ ->
        with(request.headers) {
            append("x-mosaic-platform-name", Platform.name)
            append("x-mosaic-device", Platform.device)
            append("x-mosaic-os-version", Platform.osVersion)
            append(
                "x-mosaic-extra-info",
                Platform.extraInfo.map { it.key to it.value }.joinToString()
            )
            append("x-mosaic-screen-size", Platform.screenSize)
            append("x-mosaic-screen-density", Platform.screenDensity)
            append("x-mosaic-locale", Platform.locale)
            append("x-mosaic-timezone", Platform.timezone)
            append("x-mosaic-dark-mode", Platform.darkMode)
        }
    }
}