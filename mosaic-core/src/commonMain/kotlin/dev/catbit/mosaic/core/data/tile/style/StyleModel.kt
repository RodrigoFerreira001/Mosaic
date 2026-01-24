package dev.catbit.mosaic.core.data.tile.style

import dev.catbit.mosaic.core.data.color.ColorModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StyleModel(
    @SerialName("size") val size: SizeModel,
    @SerialName("margin") val margin: MarginModel? = null,
    @SerialName("padding") val padding: PaddingModel? = null,
    @SerialName("background") val background: ColorModel? = null, // Todo mudar para Solid or Gradient
    @SerialName("border") val border: BorderModel? = null,
    @SerialName("clip") val clip: ClipModel? = null,
    @SerialName("windowInsets") val windowInsets: WindowInsetsModel? = null
) {
    companion object {
        fun default() = StyleModel(
            size = SizeModel (
                width = SizeModel.Behavior.Horizontal.Fill,
                height = SizeModel.Behavior.Vertical.Fill
            )
        )
    }
}















