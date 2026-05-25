package dev.catbit.mosaic.client.platform

expect object Platform {
    val name: String
    val device: String
    val osVersion: String
    val extraInfo: Map<String, String>
    val screenSize: String // "WIDTHxHEIGHT" -> "1920x1080"
    val screenDensity: String // "density in double" -> "1.0"
    val locale: String // "locale" -> "pt-BR"
    val timezone: String // "timezone|timezoneId" -> "GMT+09:30|Australia/Darwin"
    val darkMode: String // "boolean" -> "true"
}