package dev.catbit.mosaic.core.data.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface WindowInsetsModel {

    @Serializable
    @SerialName("system_bars")
    data object SystemBars : WindowInsetsModel

    @Serializable
    @SerialName("caption_bar")
    data object CaptionBar : WindowInsetsModel

    @Serializable
    @SerialName("status_bar")
    data object StatusBar : WindowInsetsModel

    @Serializable
    @SerialName("navigation_bar")
    data object NavigationBar : WindowInsetsModel

    @Serializable
    @SerialName("ime")
    data object Ime : WindowInsetsModel

    @Serializable
    @SerialName("display_cutout")
    data object DisplayCutout : WindowInsetsModel

    @Serializable
    @SerialName("waterfall")
    data object Waterfall : WindowInsetsModel
}