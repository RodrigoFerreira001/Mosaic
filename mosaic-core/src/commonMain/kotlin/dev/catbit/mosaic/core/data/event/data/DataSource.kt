package dev.catbit.mosaic.core.data.event.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface DataSource {

    @Serializable
    @SerialName("LocalStorage")
    data class LocalStorage(
        @SerialName("bucketIt") val bucketIt: String
    ) : DataSource

    @Serializable
    @SerialName("ScreenNavigationData")
    data object ScreenNavigationData : DataSource

    @Serializable
    @SerialName("ScreenPlainData")
    data object ScreenPlainData : DataSource

    @Serializable
    @SerialName("ScreenSegmentedData")
    data class ScreenSegmentedData(
        @SerialName("segmentId") val segmentId: String
    ) : DataSource
}

