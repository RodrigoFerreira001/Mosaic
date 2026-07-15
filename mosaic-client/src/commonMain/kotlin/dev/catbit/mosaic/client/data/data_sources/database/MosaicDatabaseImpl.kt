package dev.catbit.mosaic.client.data.data_sources.database

import dev.catbit.mosaic.client.data.data_sources.database.entities.PlainDataEntity
import dev.catbit.mosaic.client.data.data_sources.database.entities.SegmentedDataEntity
import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
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
    private val mosaicCacheDao get() = db.mosaicCacheDao()

    override suspend fun setInitialGraph(initialGraph: GraphResponse) {
        mosaicCacheDao.upsert(
            cacheKey = INITIAL_GRAPH_CACHE_KEY,
            data = serializer.encodeToString(initialGraph)
        )
    }

    override suspend fun getInitialGraph(): GraphResponse? = runCatching {
        mosaicCacheDao.get(INITIAL_GRAPH_CACHE_KEY)?.let {
            serializer.decodeFromString<GraphResponse>(it.data)
        }
    }.getOrElse {
        mosaicCacheDao.delete(INITIAL_GRAPH_CACHE_KEY)
        null
    }

    override suspend fun setScreen(cacheKey: String, screenResponse: ScreenResponse) {
        mosaicCacheDao.upsert(
            cacheKey = SCREEN_CACHE_PREFIX + cacheKey,
            data = serializer.encodeToString(screenResponse)
        )
    }

    override suspend fun getScreen(cacheKey: String): ScreenResponse? = runCatching {
        mosaicCacheDao.get(SCREEN_CACHE_PREFIX + cacheKey)?.let {
            serializer.decodeFromString<ScreenResponse>(it.data)
        }
    }.getOrElse {
        mosaicCacheDao.delete(SCREEN_CACHE_PREFIX + cacheKey)
        null
    }

    override suspend fun getCacheVersion(): Long? =
        mosaicCacheDao.get(CACHE_VERSION_CACHE_KEY)?.data?.toLongOrNull()

    override suspend fun setCacheVersion(version: Long) {
        mosaicCacheDao.upsert(
            cacheKey = CACHE_VERSION_CACHE_KEY,
            data = version.toString()
        )
    }

    override suspend fun dropScreensCache() {
        mosaicCacheDao.deleteByPrefix(SCREEN_CACHE_PREFIX)
    }

    override suspend fun dropInitialGraphCache() {
        mosaicCacheDao.delete(INITIAL_GRAPH_CACHE_KEY)
    }

    override suspend fun dropVersionCache() {
        mosaicCacheDao.delete(CACHE_VERSION_CACHE_KEY)
    }

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

    private companion object {
        const val SCREEN_CACHE_PREFIX = "MOSAIC_SCREEN_"
        const val INITIAL_GRAPH_CACHE_KEY = "MOSAIC_INITIAL_GRAPH"
        const val CACHE_VERSION_CACHE_KEY = "MOSAIC_CACHE_VERSION"
    }
}
