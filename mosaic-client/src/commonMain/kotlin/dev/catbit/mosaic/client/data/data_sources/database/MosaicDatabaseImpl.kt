package dev.catbit.mosaic.client.data.data_sources.database

import dev.catbit.mosaic.client.data.data_sources.database.entities.PlainDataEntity
import dev.catbit.mosaic.client.data.data_sources.database.entities.SegmentedDataEntity
import dev.catbit.mosaic.core.extensions.toAny
import dev.catbit.mosaic.core.extensions.toJsonElement
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.serialization.json.JsonElement

class MosaicDatabaseImpl(
    private val db: MosaicRoomDatabase,
    private val serializer: MosaicSerializer
) : MosaicDatabase {

    private val plainDataDao get() = db.plainDataDao()
    private val segmentedDataDao get() = db.segmentedDataDao()

    override suspend fun setPlainData(dataKey: String, data: Any) {
        plainDataDao.upsert(
            PlainDataEntity(
                dataKey = dataKey,
                data = serializer.encodeToString(data.toJsonElement())
            )
        )
    }

    override suspend fun getPlainData(dataKey: String): Any? = runCatching {
        plainDataDao.get(dataKey)?.let {
            serializer.decodeFromString<JsonElement>(it.data).toAny()
        }
    }.getOrNull()

    @Suppress("UNCHECKED_CAST")
    override suspend fun getAllPlainData(): Map<String, Any>? = runCatching {
        plainDataDao.getAll()
            .associate { it.dataKey to serializer.decodeFromString<JsonElement>(it.data).toAny() }
            .filterValues { it != null } as Map<String, Any>
    }.getOrNull()

    @Suppress("UNCHECKED_CAST")
    override suspend fun getPlainDataByIds(dataKeys: List<String>): Map<String, Any>? = runCatching {
        plainDataDao.getByIds(dataKeys)
            .associate { it.dataKey to serializer.decodeFromString<JsonElement>(it.data).toAny() }
            .filterValues { it != null } as Map<String, Any>
    }.getOrNull()

    override suspend fun deletePlainData(dataKey: String) {
        plainDataDao.delete(dataKey)
    }

    override suspend fun deletePlainDataByIds(dataKeys: List<String>) {
        plainDataDao.deleteByIds(dataKeys)
    }

    override suspend fun wipePlainData() {
        plainDataDao.wipe()
    }

    override suspend fun setSegmentedData(segmentKey: String, dataKey: String, data: Any) {
        segmentedDataDao.upsert(
            SegmentedDataEntity(
                segmentKey = segmentKey,
                dataKey = dataKey,
                data = serializer.encodeToString(data.toJsonElement())
            )
        )
    }

    override suspend fun getSegmentedData(segmentKey: String, dataKey: String): Any? = runCatching {
        segmentedDataDao.get(segmentKey, dataKey)?.let {
            serializer.decodeFromString<JsonElement>(it.data).toAny()
        }
    }.getOrNull()

    @Suppress("UNCHECKED_CAST")
    override suspend fun getAllSegmentedData(segmentKey: String): Map<String, Any>? = runCatching {
        segmentedDataDao.getAll(segmentKey)
            .associate { it.dataKey to serializer.decodeFromString<JsonElement>(it.data).toAny() }
            .filterValues { it != null } as Map<String, Any>
    }.getOrNull()

    @Suppress("UNCHECKED_CAST")
    override suspend fun getSegmentedDataByIds(segmentKey: String, dataKeys: List<String>): Map<String, Any>? = runCatching {
        segmentedDataDao.getByIds(segmentKey, dataKeys)
            .associate { it.dataKey to serializer.decodeFromString<JsonElement>(it.data).toAny() }
            .filterValues { it != null } as Map<String, Any>
    }.getOrNull()

    override suspend fun deleteSegmentedData(segmentKey: String, dataKey: String) {
        segmentedDataDao.delete(segmentKey, dataKey)
    }

    override suspend fun deleteSegmentedDataByIds(segmentKey: String, dataKeys: List<String>) {
        segmentedDataDao.deleteByIds(segmentKey, dataKeys)
    }

    override suspend fun wipeSegmentedData(segmentKey: String) {
        segmentedDataDao.wipe(segmentKey)
    }
}
