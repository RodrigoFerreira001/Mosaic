package dev.catbit.mosaic.core.data.schemas.tile.style

import dev.catbit.mosaic.core.data.schemas.color.ColorSchema
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StyleSchema(
    @SerialName("size") val size: SizeSchema,
    @SerialName("margin") val margin: MarginSchema? = null,
    @SerialName("padding") val padding: PaddingSchema? = null,
    @SerialName("background") val background: ColorSchema? = null, // Todo mudar para Solid or Gradient
    @SerialName("border") val border: BorderSchema? = null,
    @SerialName("clip") val clip: ClipSchema? = null,
    @SerialName("windowInsets") val windowInsets: WindowInsetsSchema? = null
) {
    companion object {
        fun default() = StyleSchema(
            size = SizeSchema (
                width = SizeSchema.Behavior.Horizontal.Fill(),
                height = SizeSchema.Behavior.Vertical.Fill()
            )
        )
    }
}















