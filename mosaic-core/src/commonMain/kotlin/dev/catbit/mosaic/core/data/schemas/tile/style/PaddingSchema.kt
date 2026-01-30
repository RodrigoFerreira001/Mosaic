package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaddingSchema(
    @SerialName("top") val top: Int,
    @SerialName("end") val end: Int,
    @SerialName("bottom") val bottom: Int,
    @SerialName("start") val start: Int
)
