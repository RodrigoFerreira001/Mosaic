package dev.catbit.mosaic.client.di

import androidx.room3.Room
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_chest.WasmJsDataChest
import dev.catbit.mosaic.client.data.data_sources.database.MosaicRoomDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import kotlinx.browser.localStorage
import org.koin.dsl.module
import org.w3c.dom.Worker

@OptIn(ExperimentalWasmJsInterop::class)
internal actual val platformModule = module {
    single<HttpClient> { HttpClient(Js) }

    single<DataChest> {
        WasmJsDataChest(localStorage)
    }

    single<MosaicRoomDatabase> {
        Room.databaseBuilder<MosaicRoomDatabase>(name = "mosaic_database.db")
            .setDriver(WebWorkerSQLiteDriver(jsWorker()))
            .build()
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun jsWorker(): Worker =
    js("""new Worker(new URL("sqlite-wasm-worker/worker.js", import.meta.url))""")