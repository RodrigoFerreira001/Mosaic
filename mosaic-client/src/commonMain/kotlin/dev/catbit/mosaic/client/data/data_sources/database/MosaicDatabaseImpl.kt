package dev.catbit.mosaic.client.data.data_sources.database

class MosaicDatabaseImpl : MosaicDatabase {
    override suspend fun setData(dataId: String, data: Any) {
        TODO("Not yet implemented")
    }

    override suspend fun getData(dataId: Any): Any? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteData(dataId: Any) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteData(segmentId: String, dataId: Any) {
        TODO("Not yet implemented")
    }

    override suspend fun setSegmentedData(segmentId: String, dataId: String, data: Any) {
        TODO("Not yet implemented")
    }

    override suspend fun getSegmentedData(segmentId: String, dataId: Any): Any? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSegment(segmentId: String) {
        TODO("Not yet implemented")
    }
}