package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Specifies which system window insets should be applied as padding to a tile.
 *
 * Applied first in `styledWith()` via `Modifier.windowInsetsPadding(...)`, so that subsequent
 * margin and size constraints operate within the safe area.
 *
 * Use [SystemBars] for the most common case (root screen containers that should avoid both
 * status bar and navigation bar). For screens with a keyboard input, add [Ime] to handle
 * soft keyboard insets.
 *
 * **Variants → Compose mapping:**
 * - [SystemBars]    → `WindowInsets.systemBars`
 * - [StatusBar]     → `WindowInsets.statusBars`
 * - [NavigationBar] → `WindowInsets.navigationBars`
 * - [Ime]           → `WindowInsets.ime`
 * - [CaptionBar]    → `WindowInsets.captionBar`
 * - [DisplayCutout] → `WindowInsets.displayCutout`
 * - [Waterfall]     → `WindowInsets.waterfall`
 *
 * **DSL helpers (mosaic-server):**
 * ```kotlin
 * windowInsets(windowInsetsSystemBars())
 * windowInsets(windowInsetsStatusBar())
 * windowInsets(windowInsetsNavigationBar())
 * windowInsets(windowInsetsIme())
 * windowInsets(windowInsetsCaptionBar())
 * windowInsets(windowInsetsDisplayCutout())
 * windowInsets(windowInsetsWaterfall())
 * ```
 *
 * **Note:** Only one inset type can be applied per `StyleSchema`. To combine multiple insets
 * (e.g. status bar + IME), use [SystemBars] which covers both vertical system bars.
 */
@Serializable
sealed interface WindowInsetsSchema {

    @Serializable
    @SerialName("system_bars")
    data object SystemBars : WindowInsetsSchema

    @Serializable
    @SerialName("caption_bar")
    data object CaptionBar : WindowInsetsSchema

    @Serializable
    @SerialName("status_bar")
    data object StatusBar : WindowInsetsSchema

    @Serializable
    @SerialName("navigation_bar")
    data object NavigationBar : WindowInsetsSchema

    @Serializable
    @SerialName("ime")
    data object Ime : WindowInsetsSchema

    @Serializable
    @SerialName("display_cutout")
    data object DisplayCutout : WindowInsetsSchema

    @Serializable
    @SerialName("waterfall")
    data object Waterfall : WindowInsetsSchema
}