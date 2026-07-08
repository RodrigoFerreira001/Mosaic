package dev.catbit.mosaic.client.di

import androidx.room3.Room
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_chest.WasmJsDataChest
import dev.catbit.mosaic.client.data.data_sources.database.MosaicRoomDatabase
import dev.catbit.mosaic.client.camera.WasmJsCameraManager
import dev.catbit.mosaic.client.permission.WasmJsPermissionManager
import dev.catbit.mosaic.client.ui.sdui.foundation.camera.CameraManager
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionManager
import dev.catbit.mosaic.core.domain.base.IO
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import kotlinx.browser.localStorage
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.w3c.dom.Worker

@OptIn(ExperimentalWasmJsInterop::class)
internal actual val platformModule = module {
    single<HttpClient> { HttpClient(Js) }

    single<PermissionManager> { WasmJsPermissionManager() }

    single<CameraManager> { WasmJsCameraManager() }

    single<DataChest> {
        WasmJsDataChest(localStorage)
    }

    single<MosaicRoomDatabase> {

        val applicationId: String = get(named("APPLICATION_ID"))

        Room.databaseBuilder<MosaicRoomDatabase>(name = "${applicationId}_database.db")
            .setDriver(WebWorkerSQLiteDriver(jsWorker()))
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun jsWorker(): Worker =
    js("""new Worker(new URL("@catbit.dev/sqlite-wasm-worker/worker.js", import.meta.url))""")