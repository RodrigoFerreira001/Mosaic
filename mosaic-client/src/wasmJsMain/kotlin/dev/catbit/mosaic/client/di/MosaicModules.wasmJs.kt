package dev.catbit.mosaic.client.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_chest.WasmJsDataChest
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

    single<SqlDriver> {
        WebWorkerDriver(
            Worker(
                scriptURL = js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
            )
        )
    }
}