package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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