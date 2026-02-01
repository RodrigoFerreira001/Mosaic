package dev.catbit.mosaic.client.di

import dev.catbit.mosaic.client.application.ApplicationIdentifierHolder
import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_chest.JvmDataChest
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import java.util.prefs.Preferences
import org.koin.dsl.module

internal actual val platformModule = module {
    single<HttpClient> { HttpClient(OkHttp) }

    single<DataChest> {
        JvmDataChest(Preferences.userRoot().node("Mosaic").node(ApplicationIdentifierHolder.applicationId))
    }
}