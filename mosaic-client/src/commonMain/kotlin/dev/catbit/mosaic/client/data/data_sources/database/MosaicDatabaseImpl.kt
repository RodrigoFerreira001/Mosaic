package dev.catbit.mosaic.client.data.data_sources.database

import dev.catbit.mosaic.core.extensions.toAny
import dev.catbit.mosaic.core.extensions.toJsonElement
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.serialization.json.JsonElement
import dev.catbit.mosaic.client.MosaicDatabase as MosaicSQDelightDatabase

class MosaicDatabaseImpl(
    private val database: MosaicSQDelightDatabase,
    private val serializer: MosaicSerializer
) : MosaicDatabase {

    override suspend fun setData(
        dataId: String,
        data: Any
    ) {
        database.mosaicDatabaseQueries.setGlobal(
            dataId = dataId,
            data_ = serializer.encodeToString(data.toJsonElement())
        )
    }

    override suspend fun getData(
        dataId: String
    ): Any? = runCatching {
        val result = database.mosaicDatabaseQueries.getGlobal(dataId).executeAsOne()
        serializer.decodeFromString<JsonElement>(result).toAny()
    }.getOrElse {
        database.mosaicDatabaseQueries.deleteGlobal(dataId)
        null
    }

    override suspend fun deleteData(
        dataId: String
    ) {
        database.mosaicDatabaseQueries.deleteGlobal(dataId)
    }

    override suspend fun deleteData(
        segmentId: String,
        dataId: String
    ) {
        database.mosaicDatabaseQueries.deleteSegmentedItem(
            segmentId = segmentId,
            dataId = dataId
        )
    }

    override suspend fun setSegmentedData(
        segmentId: String,
        dataId: String,
        data: Any
    ) {
        database.mosaicDatabaseQueries.setSegmented(
            segmentId = segmentId,
            dataId = dataId,
            data_ = serializer.encodeToString(data.toJsonElement())
        )
    }

    override suspend fun getSegmentedData(
        segmentId: String,
        dataId: String
    ): Any? = runCatching{
        val result = database.mosaicDatabaseQueries.getSegmented(
            segmentId = dataId,
            dataId = dataId
        ).executeAsOne()
        serializer.decodeFromString<JsonElement>(result).toAny()
    }.getOrElse {
        database.mosaicDatabaseQueries.deleteSegmentedItem(
            segmentId = segmentId,
            dataId = dataId
        )
    }

    override suspend fun deleteSegment(
        segmentId: String
    ) {
        database.mosaicDatabaseQueries.deleteSegment(segmentId)
    }
}