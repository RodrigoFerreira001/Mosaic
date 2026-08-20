package dev.catbit.mosaic.client.data.data_sources.network.plugins

import dev.catbit.mosaic.client.platform.Platform
import io.ktor.client.plugins.api.createClientPlugin

/**
 * Ktor client plugin that stamps every outgoing request with 9 `x-mosaic-*` headers describing the
 * current device/platform, read live from [Platform] on each request. Installed exactly once, on the
 * single `HttpClient` `MosaicModules.dataModule` builds for `MosaicNetworkImpl` — since every Mosaic
 * network operation (`SendNetworkRequest`, `UploadFile`, all 3 download events, `GetScreen`/
 * `RefreshScreen`, the initial-graph fetch, the cache-version check) goes through that one client,
 * every one of them carries these headers, unconditionally. There is no per-request opt-out and no
 * DSL flag to suppress it — a backend can always rely on these headers being present, on any request
 * a Mosaic client ever sends.
 *
 * Headers sent, each straight from the matching [Platform] property:
 * - `x-mosaic-platform-name` ← [Platform.name]
 * - `x-mosaic-device` ← [Platform.device]
 * - `x-mosaic-os-version` ← [Platform.osVersion]
 * - `x-mosaic-extra-info` ← [Platform.extraInfo], flattened to a single comma-separated
 *   `key=value` list via `Map.toString()`-style `joinToString()` — not JSON, not individually
 *   parseable back into typed key-value pairs without splitting the string yourself.
 * - `x-mosaic-screen-size` ← [Platform.screenSize]
 * - `x-mosaic-screen-density` ← [Platform.screenDensity]
 * - `x-mosaic-locale` ← [Platform.locale]
 * - `x-mosaic-timezone` ← [Platform.timezone]
 * - `x-mosaic-dark-mode` ← [Platform.darkMode]
 */
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