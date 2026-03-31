package dev.catbit.mosaic.client.data.data_sources.database

interface MosaicDatabase {

    suspend fun setPlainData(
        dataKey: String,
        data: Any
    )

    suspend fun getPlainData(
        dataKey: String
    ): Any?

    suspend fun getAllPlainData(): Map<String, Any>?

    suspend fun getPlainDataByIds(dataKeys: List<String>): Map<String, Any>?

    suspend fun deletePlainData(dataKey: String)

    suspend fun deletePlainDataByIds(dataKeys: List<String>)

    suspend fun wipePlainData()

    suspend fun setSegmentedData(
        segmentKey: String,
        dataKey: String,
        data: Any
    )

    suspend fun getSegmentedData(
        segmentKey: String,
        dataKey: String
    ): Any?

    suspend fun getAllSegmentedData(
        segmentKey: String
    ): Map<String, Any>?

    suspend fun getSegmentedDataByIds(
        segmentKey: String,
        dataKeys: List<String>
    ): Map<String, Any>?

    suspend fun deleteSegmentedData(
        segmentKey: String,
        dataKey: String
    )

    suspend fun deleteSegmentedDataByIds(
        segmentKey: String,
        dataKeys: List<String>
    )

    suspend fun wipeSegmentedData(
        segmentKey: String
    )
}