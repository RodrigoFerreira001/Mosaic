package dev.catbit.mosaic.client.data.data_sources.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import dev.catbit.mosaic.client.data.data_sources.database.daos.PlainDataDao
import dev.catbit.mosaic.client.data.data_sources.database.daos.SegmentedDataDao
import dev.catbit.mosaic.client.data.data_sources.database.entities.PlainDataEntity
import dev.catbit.mosaic.client.data.data_sources.database.entities.SegmentedDataEntity

@Database(
    entities = [PlainDataEntity::class, SegmentedDataEntity::class],
    version = 1
)
@ConstructedBy(MosaicRoomDatabaseConstructor::class)
abstract class MosaicRoomDatabase : RoomDatabase() {
    abstract fun plainDataDao(): PlainDataDao
    abstract fun segmentedDataDao(): SegmentedDataDao
}

@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object MosaicRoomDatabaseConstructor : RoomDatabaseConstructor<MosaicRoomDatabase> {
    override fun initialize(): MosaicRoomDatabase
}
