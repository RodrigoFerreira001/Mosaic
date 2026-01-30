package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClipSchema(
    @SerialName("shape") val shape: ShapeSchema
)