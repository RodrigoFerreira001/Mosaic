package dev.catbit.mosaic.client.platform

/**
 * Read-only snapshot of platform/device information, resolved once per platform target
 * (`expect`/`actual`). Every field here is sent as an `x-mosaic-*` header
 * (`x-mosaic-platform-name`, `x-mosaic-device`, etc.) on **every** outgoing HTTP request, via
 * `MosaicHeadersPlugin` — so a backend can branch on client platform/device/locale/theme without the
 * DSL author having to declare that data explicitly on each request. It's a plain `object`, not
 * Koin-injected, so a custom `EventRunner`/`TileRenderer` can also read it directly for its own
 * purposes.
 */
expect object Platform {
    /** The platform name, e.g. `"Android"`, `"iOS"`, `"JVM"`, `"WasmJS"`. */
    val name: String
    /** Device model identifier — platform-specific format. */
    val device: String
    /** Operating system version string. */
    val osVersion: String
    /** Extra platform-specific key-value pairs not covered by the other properties. */
    val extraInfo: Map<String, String>
    /** Screen size as `"WIDTHxHEIGHT"`, e.g. `"1920x1080"`. */
    val screenSize: String // "WIDTHxHEIGHT" -> "1920x1080"
    /** Screen density as a decimal string, e.g. `"1.0"`. */
    val screenDensity: String // "density in double" -> "1.0"
    /** Current locale, e.g. `"pt-BR"`. */
    val locale: String // "locale" -> "pt-BR"
    /** Current timezone as `"offset|timezoneId"`, e.g. `"GMT+09:30|Australia/Darwin"`. */
    val timezone: String // "timezone|timezoneId" -> "GMT+09:30|Australia/Darwin"
    /** Whether dark mode is currently active, as the string `"true"`/`"false"`. */
    val darkMode: String // "boolean" -> "true"
}