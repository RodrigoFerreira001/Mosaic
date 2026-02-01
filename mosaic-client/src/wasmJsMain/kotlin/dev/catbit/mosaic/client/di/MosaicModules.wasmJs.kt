package dev.catbit.mosaic.client.di

import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_chest.WasmJsDataChest
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import kotlinx.browser.localStorage
import org.koin.dsl.module

internal actual val platformModule = module {
    single<HttpClient> { HttpClient(Js) }

    single<DataChest> {
        WasmJsDataChest(localStorage)
    }
}