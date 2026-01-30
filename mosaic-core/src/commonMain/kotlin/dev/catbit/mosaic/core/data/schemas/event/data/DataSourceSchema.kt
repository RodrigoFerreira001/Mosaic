package dev.catbit.mosaic.core.data.schemas.event.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface DataSourceSchema {

    @Serializable
    @SerialName("LocalStorage")
    data class LocalStorage(
        @SerialName("bucketIt") val bucketIt: String
    ) : DataSourceSchema

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
}

