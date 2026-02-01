package dev.catbit.mosaic.core.data.schemas.event.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AccessModeSchema {
    @Serializable
    @SerialName("Full")
    data object Full : AccessModeSchema

    @Serializable
    @SerialName("Batch")
    data class Batch(
        @SerialName("dataIds") val dataIds: List<String>,
        @SerialName("allowMissingData") val allowMissingData: Boolean,
        @SerialName("unwrapValuesToList") val unwrapValuesToList: Boolean,
    ) : AccessModeSchema
}