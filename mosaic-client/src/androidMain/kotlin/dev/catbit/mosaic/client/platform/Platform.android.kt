package dev.catbit.mosaic.client.platform

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetIn
import org.koin.mp.KoinPlatform

actual object Platform {

    private val context: Context
        get() = KoinPlatform.getKoin().get<Context>()

    actual val name: String = "Android"

    actual val device: String = "${Build.MANUFACTURER} ${Build.MODEL}"

    actual val osVersion: String = Build.VERSION.RELEASE.toString()

    actual val extraInfo: Map<String, String> = emptyMap()

    actual val screenSize: String
        get() {
            val metrics = context.resources.displayMetrics
            return "${metrics.widthPixels}x${metrics.heightPixels}"
        }

    actual val screenDensity: String
        get() = context.resources.displayMetrics.density.toString()

    actual val locale: String
        get() = context.resources.configuration.locales.get(0).toLanguageTag()

    actual val timezone: String
        get() {
            val tz = TimeZone.currentSystemDefault()
            val offset = kotlin.time.Clock.System.now().offsetIn(tz)
            return "$offset|${tz.id}"
        }

    actual val darkMode: String
        get() {
            val uiMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return (uiMode == Configuration.UI_MODE_NIGHT_YES).toString()
        }
}