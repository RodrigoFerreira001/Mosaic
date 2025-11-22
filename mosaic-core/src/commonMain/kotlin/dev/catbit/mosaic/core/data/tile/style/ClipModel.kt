package dev.catbit.mosaic.core.data.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClipModel(
    @SerialName("shape") val shape: ShapeModel
)