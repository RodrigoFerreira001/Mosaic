package dev.catbit.mosaic.core.data.schemas.icon

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IconSchema(
    @SerialName("name") val name: String,
    @SerialName("color") val color: ColorSchema ? = null,
    @SerialName("size") val size: Int ? = null,
    @SerialName("style") val style: Style = Style.OUTLINED
) {
    enum class Style {
        OUTLINED, ROUNDED, SHARP
    }
}