package dev.catbit.mosaic.core.data.tile.style

import dev.catbit.mosaic.core.data.color.ColorModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BorderModel(
    @SerialName("color") val color: ColorModel,
    @SerialName("thickness") val thickness: Int,
    @SerialName("radius") val radius: RadiusModel?
)