package dev.catbit.mosaic.client.di

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.catbit.mosaic.client.application.ActivityHolder
import dev.catbit.mosaic.client.data.data_chest.AndroidDataChest
import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_sources.database.MosaicRoomDatabase
import dev.catbit.mosaic.client.camera.AndroidCameraManager
import dev.catbit.mosaic.client.permission.AndroidPermissionManager
import dev.catbit.mosaic.client.ui.sdui.foundation.camera.CameraManager
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformModule: Module = module {

    single<HttpClient> { HttpClient(OkHttp) }

    single<PermissionManager> {
        AndroidPermissionManager { ActivityHolder.getActivity() as ComponentActivity }
    }

    single<CameraManager> {
        AndroidCameraManager { ActivityHolder.getActivity() as ComponentActivity }
    }

    single<DataChest> {
        with(get<Context>()) {
            AndroidDataChest(
                getSharedPreferences(
                    packageManager.getApplicationLabel(applicationInfo).toString(), Context.MODE_PRIVATE
                )
            )
        }
    }

    single<MosaicRoomDatabase> {

        val applicationId: String = get(named("APPLICATION_ID"))

        Room.databaseBuilder<MosaicRoomDatabase>(
            context = get<Context>(),
            name = "${applicationId}_database.db"
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}
