package dev.catbit.mosaic.client.data.data_sources.database.daos

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import dev.catbit.mosaic.client.data.data_sources.database.entities.SegmentedDataEntity

@Dao
interface SegmentedDataDao {

    @Upsert
    suspend fun upsert(entity: SegmentedDataEntity)

    @Query("SELECT * FROM segmented_data WHERE segmentKey = :segmentKey AND dataKey = :dataKey")
    suspend fun get(segmentKey: String, dataKey: String): SegmentedDataEntity?

    @Query("SELECT * FROM segmented_data WHERE segmentKey = :segmentKey")
    suspend fun getAll(segmentKey: String): List<SegmentedDataEntity>

    @Query("SELECT * FROM segmented_data WHERE segmentKey = :segmentKey AND dataKey IN (:dataKeys)")
    suspend fun getByIds(segmentKey: String, dataKeys: List<String>): List<SegmentedDataEntity>

    @Query("DELETE FROM segmented_data WHERE segmentKey = :segmentKey AND dataKey = :dataKey")
    suspend fun delete(segmentKey: String, dataKey: String)

    @Query("DELETE FROM segmented_data WHERE segmentKey = :segmentKey AND dataKey IN (:dataKeys)")
    suspend fun deleteByIds(segmentKey: String, dataKeys: List<String>)

    @Query("DELETE FROM segmented_data WHERE segmentKey = :segmentKey")
    suspend fun wipe(segmentKey: String)
}
