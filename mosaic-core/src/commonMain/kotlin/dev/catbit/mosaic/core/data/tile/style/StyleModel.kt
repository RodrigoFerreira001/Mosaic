package dev.catbit.mosaic.core.data.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StyleModel(
    @SerialName("size") val size: SizeModel,
    @SerialName("margin") val margin: MarginModel? = null,
    @SerialName("padding") val padding: PaddingModel? = null,
    @SerialName("background") val background: String? = null,
    @SerialName("border") val border: BorderModel? = null,
    @SerialName("clip") val clip: ClipModel? = null,
    @SerialName("windowInsets") val windowInsets: WindowInsetsModel? = null
)















