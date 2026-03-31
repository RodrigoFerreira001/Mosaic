package dev.catbit.mosaic.core.data.schemas.event.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface DataSourceSchema {

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

