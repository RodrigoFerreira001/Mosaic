package dev.catbit.mosaic.client.data.data_sources.database.daos

import androidx.room3.Dao
import androidx.room3.Query
import dev.catbit.mosaic.client.data.data_sources.database.entities.MosaicCacheEntity

@Dao
interface MosaicCacheDao {

    @Query("INSERT OR REPLACE INTO mosaic_cache (cacheKey, data) VALUES (:cacheKey, :data)")
    suspend fun upsert(cacheKey: String, data: String)

    @Query("SELECT * FROM mosaic_cache WHERE cacheKey = :cacheKey")
    suspend fun get(cacheKey: String): MosaicCacheEntity?

    @Query("DELETE FROM mosaic_cache WHERE cacheKey = :cacheKey")
    suspend fun delete(cacheKey: String)

    @Query("DELETE FROM mosaic_cache WHERE cacheKey LIKE :prefix || '%'")
    suspend fun deleteByPrefix(prefix: String)
}
