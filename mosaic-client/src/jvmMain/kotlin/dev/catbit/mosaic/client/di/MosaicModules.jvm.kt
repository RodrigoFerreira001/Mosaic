package dev.catbit.mosaic.client.di

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.catbit.mosaic.client.camera.JvmCameraManager
import dev.catbit.mosaic.client.data.data_sources.database.MosaicRoomDatabase
import dev.catbit.mosaic.client.permission.JvmPermissionManager
import dev.catbit.mosaic.client.ui.sdui.foundation.camera.CameraManager
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformModule = module {
    single<HttpClient> { HttpClient(OkHttp) }

    single<PermissionManager> { JvmPermissionManager() }

    single<CameraManager> { JvmCameraManager() }

    single<MosaicRoomDatabase> {

        val applicationId: String = get(named("APPLICATION_ID"))

        Room.databaseBuilder<MosaicRoomDatabase>(name = "${applicationId}_database.db")
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}
