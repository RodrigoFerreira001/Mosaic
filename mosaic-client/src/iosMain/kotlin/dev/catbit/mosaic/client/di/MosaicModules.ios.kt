package dev.catbit.mosaic.client.di

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_chest.IOSDataChest
import dev.catbit.mosaic.client.data.data_sources.database.MosaicRoomDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

internal actual val platformModule = module {
    single<HttpClient> { HttpClient(Darwin) }

    single<DataChest> {
        IOSDataChest(NSUserDefaults.standardUserDefaults())
    }

    single<MosaicRoomDatabase> {
        Room.databaseBuilder<MosaicRoomDatabase>(name = "mosaic_database.db")
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}
