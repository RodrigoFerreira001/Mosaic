package dev.catbit.mosaic.client.di

import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_chest.JvmDataChest
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import java.util.prefs.Preferences
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val platformModule = module {
    single<HttpClient> { HttpClient(OkHttp) }

    single<DataChest> {
        val applicationId = get<String>(named("APPLICATION_ID"))
        JvmDataChest(Preferences.userRoot().node("Mosaic").node(applicationId))
    }
}