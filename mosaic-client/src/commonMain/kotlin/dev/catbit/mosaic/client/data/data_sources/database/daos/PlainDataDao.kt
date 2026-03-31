package dev.catbit.mosaic.client.data.data_sources.database.daos

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import dev.catbit.mosaic.client.data.data_sources.database.entities.PlainDataEntity

@Dao
interface PlainDataDao {

    @Upsert
    suspend fun upsert(entity: PlainDataEntity)

    @Query("SELECT * FROM plain_data WHERE dataKey = :dataKey")
    suspend fun get(dataKey: String): PlainDataEntity?

    @Query("SELECT * FROM plain_data")
    suspend fun getAll(): List<PlainDataEntity>

    @Query("SELECT * FROM plain_data WHERE dataKey IN (:dataKeys)")
    suspend fun getByIds(dataKeys: List<String>): List<PlainDataEntity>

    @Query("DELETE FROM plain_data WHERE dataKey = :dataKey")
    suspend fun delete(dataKey: String)

    @Query("DELETE FROM plain_data WHERE dataKey IN (:dataKeys)")
    suspend fun deleteByIds(dataKeys: List<String>)

    @Query("DELETE FROM plain_data")
    suspend fun wipe()
}
