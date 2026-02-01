package dev.catbit.mosaic.client.di

import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_chest.IOSDataChest
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

internal actual val platformModule = module {
    single<HttpClient> { HttpClient(Darwin) }

    single<DataChest> {
        IOSDataChest(NSUserDefaults.standardUserDefaults())
    }
}