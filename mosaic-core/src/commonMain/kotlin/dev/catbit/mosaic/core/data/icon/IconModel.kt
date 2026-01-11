package dev.catbit.mosaic.core.data.icon

import dev.catbit.mosaic.core.data.color.ColorModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IconModel(
    @SerialName("name") val name: String,
    @SerialName("color") val color: ColorModel ? = null,
    @SerialName("size") val size: Int ? = null,
    @SerialName("style") val style: Style = Style.OUTLINED
) {
    enum class Style {
        OUTLINED, ROUNDED, SHARP
    }
}