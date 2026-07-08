package dev.catbit.mosaic.core.data.schemas.event.data

import androidx.compose.runtime.Immutable
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
sealed interface DataSourceSchema {

    /** A fixed, screen-defined value — not backed by any repository/storage. */
    @Serializable
    @SerialName("Inline")
    data class Inline(
        @SerialName("data") val data: Map<String, AnySerializable?>
    ) : DataSourceSchema

    @Serializable
    @SerialName("ApplicationPlainData")
    data object ApplicationPlainData : DataSourceSchema

    @Serializable
    @SerialName("ApplicationSegmentedData")
    data class ApplicationSegmentedData(
        @SerialName("segmentId") val segmentId: String
    ) : DataSourceSchema

    @Serializable
    @SerialName("SegmentedDataBase")
    data class SegmentedDataBase(
        @SerialName("segmentId") val segmentId: String
    ) : DataSourceSchema

    @Serializable
    @SerialName("PlainDataBase")
    data object PlainDataBase : DataSourceSchema

    @Serializable
    @SerialName("ScreenNavigationData")
    data object ScreenNavigationData : DataSourceSchema

    @Serializable
    @SerialName("ScreenPlainData")
    data object ScreenPlainData : DataSourceSchema

    @Serializable
    @SerialName("ScreenSegmentedData")
    data class ScreenSegmentedData(
        @SerialName("segmentId") val segmentId: String
    ) : DataSourceSchema

    @Serializable
    @SerialName("Tile")
    data class Tile(
        @SerialName("tileId") val tileId: String,
        @SerialName("dataKey") val dataKey: String
    ) : DataSourceSchema
}

