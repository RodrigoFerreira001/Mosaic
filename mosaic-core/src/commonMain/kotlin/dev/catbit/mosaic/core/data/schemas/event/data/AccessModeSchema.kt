package dev.catbit.mosaic.core.data.schemas.event.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.serialization.serializers.SerializableImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
sealed interface AccessModeSchema {
    @Serializable
    @SerialName("Full")
    data object Full : AccessModeSchema

    @Serializable
    @SerialName("Batch")
    data class Batch(
        @SerialName("dataIds") val dataIds: SerializableImmutableList<String>,
        @SerialName("allowMissingData") val allowMissingData: Boolean,
        @SerialName("unwrapValuesToList") val unwrapValuesToList: Boolean,
    ) : AccessModeSchema

    @Serializable
    @SerialName("Single")
    data class Single(
        @SerialName("dataId") val dataId: String
    ) : AccessModeSchema

}