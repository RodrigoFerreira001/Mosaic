package dev.catbit.mosaic.client.platform

import kotlinx.browser.window
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetIn
import kotlin.time.Clock

actual object Platform {
    actual val name: String = "WasmJs"
    actual val device: String = window.navigator.platform.takeIf { it.isNotEmpty() } ?: "Web"
    actual val osVersion: String = window.navigator.userAgent
    actual val extraInfo: Map<String, String> = emptyMap()
    actual val screenSize: String
        get() = "${window.screen.width}x${window.screen.height}"
    actual val screenDensity: String
        get() = window.devicePixelRatio.toString()
    actual val locale: String
        get() = window.navigator.language
    actual val timezone: String
        get() {
            val tz = TimeZone.currentSystemDefault()
            val offset = Clock.System.now().offsetIn(tz)
            return "$offset|${tz.id}"
        }
    actual val darkMode: String
        get() = window.matchMedia("(prefers-color-scheme: dark)").matches.toString()
}