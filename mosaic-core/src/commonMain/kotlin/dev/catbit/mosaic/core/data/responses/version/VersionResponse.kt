package dev.catbit.mosaic.core.data.responses.version

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionResponse(
    @SerialName("version")
    val version: Long
)
