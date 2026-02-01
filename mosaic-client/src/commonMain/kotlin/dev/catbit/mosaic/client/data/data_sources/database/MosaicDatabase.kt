package dev.catbit.mosaic.client.data.data_sources.database

interface MosaicDatabase {
    //https://klibs.io/project/sqldelight/sqldelight
    suspend fun setData(
        dataId: String,
        data: Any
    )

    suspend fun getData(
        dataId: Any
    ): Any?

    suspend fun deleteData(dataId: Any)

    suspend fun setSegmentedData(
        segmentId: String,
        dataId: String,
        data: Any
    )

    suspend fun getSegmentedData(
        segmentId: String,
        dataId: Any
    ): Any?

    suspend fun deleteData(
        segmentId: String,
        dataId: Any
    )

    suspend fun deleteSegment(segmentId: String)
}