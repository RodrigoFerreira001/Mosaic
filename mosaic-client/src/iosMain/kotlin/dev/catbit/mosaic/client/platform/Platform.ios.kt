package dev.catbit.mosaic.client.platform

import kotlin.time.Clock.System
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.countryCode
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetIn
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen
import platform.UIKit.UITraitCollection
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.currentTraitCollection

actual object Platform {
    actual val name: String = "iOS"
    actual val device: String = UIDevice.currentDevice.model
    actual val osVersion: String = UIDevice.currentDevice.systemVersion
    actual val extraInfo: Map<String, String> = emptyMap()
    @OptIn(ExperimentalForeignApi::class)
    actual val screenSize: String
        get() {
            val bounds = UIScreen.mainScreen.bounds
            return bounds.useContents { "${size.width.toInt()}x${size.height.toInt()}" }
        }
    actual val screenDensity: String
        get() = UIScreen.mainScreen.scale.toString()
    actual val locale: String
        get() {
            val loc = NSLocale.currentLocale
            val lang = loc.languageCode
            val country = loc.countryCode
            return if (country != null) "$lang-$country" else lang
        }
    actual val timezone: String
        get() {
            val tz = TimeZone.currentSystemDefault()
            val offset = System.now().offsetIn(tz)
            return "$offset|${tz.id}"
        }
    actual val darkMode: String
        get() {
            val style = UITraitCollection.currentTraitCollection.userInterfaceStyle
            return (style == UIUserInterfaceStyle.UIUserInterfaceStyleDark).toString()
        }
}
