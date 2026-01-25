package dev.catbit.mosaic.core.data.event.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AccessMode {
    @Serializable
    @SerialName("Full")
    data object Full : AccessMode

    @Serializable
    @SerialName("Batch")
    data class Batch(
        @SerialName("dataIds") val dataIds: List<String>,
        @SerialName("allowMissingData") val allowMissingData: Boolean,
    ) : AccessMode
}