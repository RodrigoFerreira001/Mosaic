package dev.catbit.mosaic.client.data.data_sources.database

interface MosaicDatabase {
    //https://klibs.io/project/sqldelight/sqldelight
    suspend fun setData(
        dataId: String,
        data: Any
    )

    suspend fun getData(
        dataId: String
    ): Any?

    suspend fun deleteData(dataId: String)

    suspend fun setSegmentedData(
        segmentId: String,
        dataId: String,
        data: Any
    )

    suspend fun getSegmentedData(
        segmentId: String,
        dataId: String
    ): Any?

    suspend fun deleteData(
        segmentId: String,
        dataId: String
    )

    suspend fun deleteSegment(segmentId: String)
}