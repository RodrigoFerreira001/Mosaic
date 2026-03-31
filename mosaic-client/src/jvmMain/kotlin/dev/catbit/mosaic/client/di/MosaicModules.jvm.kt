package dev.catbit.mosaic.client.di

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_chest.JvmDataChest
import dev.catbit.mosaic.client.data.data_sources.database.MosaicRoomDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import java.util.prefs.Preferences
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformModule = module {
    single<HttpClient> { HttpClient(OkHttp) }

    single<DataChest> {
        val applicationId = get<String>(named("APPLICATION_ID"))
        JvmDataChest(Preferences.userRoot().node("Mosaic").node(applicationId))
    }

    single<MosaicRoomDatabase> {
        Room.databaseBuilder<MosaicRoomDatabase>(name = "mosaic_database.db")
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}
