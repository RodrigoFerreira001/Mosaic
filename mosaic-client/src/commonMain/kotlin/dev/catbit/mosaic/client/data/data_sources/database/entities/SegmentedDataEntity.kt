package dev.catbit.mosaic.client.data.data_sources.database.entities

import androidx.room3.Entity

@Entity(
    tableName = "segmented_data",
    primaryKeys = ["segmentKey", "dataKey"]
)
data class SegmentedDataEntity(
    val segmentKey: String,
    val dataKey: String,
    val data: String
)
