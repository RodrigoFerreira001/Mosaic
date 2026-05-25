package dev.catbit.mosaic.client.platform

import java.awt.Toolkit
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetIn

actual object Platform {

    private const val SCREEN_DENSITY_CONSTANT = 96.0

    actual val name: String = "Jvm"
    actual val device: String
        get() = getDesktopModelName()

    private fun getDesktopModelName(): String {
        val os = System.getProperty("os.name").lowercase()
        return try {
            when {
                os.contains("mac") -> {
                    executeCommand("sysctl -n hw.model")
                }
                os.contains("win") -> {
                    executeCommand("wmic csproduct get name")
                        .replace("Name", "").trim()
                }
                os.contains("linux") -> {
                    executeCommand("cat /sys/devices/virtual/dmi/id/product_name")
                }
                else -> "Unknown JVM Device"
            }
        } catch (e: Exception) {
            "Unknown JVM Device"
        }
    }

    private fun executeCommand(command: String): String {
        val process = Runtime.getRuntime().exec(listOf(command).toTypedArray())
        process.waitFor()
        return BufferedReader(InputStreamReader(process.inputStream)).readText().trim()
    }
    actual val osVersion: String = System.getProperty("os.version") ?: "unknown"
    actual val extraInfo: Map<String, String> = emptyMap()
    actual val screenSize: String
        get() {
            val size = Toolkit.getDefaultToolkit().screenSize
            return "${size.width}x${size.height}"
        }
    actual val screenDensity: String
        get() {
            val dpi = Toolkit.getDefaultToolkit().screenResolution
            return (dpi / SCREEN_DENSITY_CONSTANT).toString()
        }
    actual val locale: String
        get() = Locale.getDefault().toLanguageTag()
    actual val timezone: String
        get() {
            val tz = TimeZone.currentSystemDefault()
            val offset = Clock.System.now().offsetIn(tz)
            return "$offset|${tz.id}"
        }
    actual val darkMode: String
        get() = "false"
}
