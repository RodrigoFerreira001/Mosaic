package dev.catbit.mosaic.client.data.data_sources.database.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "plain_data")
data class PlainDataEntity(
    @PrimaryKey val dataKey: String,
    val data: String
)
