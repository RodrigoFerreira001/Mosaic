package dev.catbit.mosaic.core.data.schemas.tile.style

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BorderSchema(
    @SerialName("color") val color: ColorSchema,
    @SerialName("thickness") val thickness: Int,
    @SerialName("radius") val radius: RadiusSchema?
)