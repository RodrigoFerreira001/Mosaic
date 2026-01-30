package dev.catbit.mosaic.core.data.schemas.tile.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RadiusSchema(
    @SerialName("topStart") val topStart: Int,
    @SerialName("topEnd") val topEnd: Int,
    @SerialName("bottomStart") val bottomStart: Int,
    @SerialName("bottomEnd") val bottomEnd: Int
)