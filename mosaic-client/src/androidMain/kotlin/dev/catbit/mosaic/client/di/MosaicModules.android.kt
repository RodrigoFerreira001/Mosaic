package dev.catbit.mosaic.client.di

import android.content.Context
import dev.catbit.mosaic.client.context.AndroidContextWrapper
import dev.catbit.mosaic.client.data.data_chest.AndroidDataChest
import dev.catbit.mosaic.client.data.data_chest.DataChest
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {

    single<HttpClient> { HttpClient(OkHttp) }

    single<DataChest> {
        val context = AndroidContextWrapper.getContext()
            ?: throw RuntimeException("No Android Context set. Did you forget to call AndroidContextWrapper.setContext?")
        AndroidDataChest(
            context.getSharedPreferences(
                context.packageManager.getApplicationLabel(context.applicationInfo).toString(), Context.MODE_PRIVATE
            )
        )
    }
}