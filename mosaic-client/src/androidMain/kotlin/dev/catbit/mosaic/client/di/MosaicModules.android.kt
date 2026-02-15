package dev.catbit.mosaic.client.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.catbit.mosaic.client.MosaicDatabase
import dev.catbit.mosaic.client.data.data_chest.AndroidDataChest
import dev.catbit.mosaic.client.data.data_chest.DataChest
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {

    single<HttpClient> { HttpClient(OkHttp) }

    single<DataChest> {
        with(get<Context>()) {
            AndroidDataChest(
                getSharedPreferences(
                    packageManager.getApplicationLabel(applicationInfo).toString(), Context.MODE_PRIVATE
                )
            )
        }
    }

    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = MosaicDatabase.Schema,
            context = get<Context>(),
            name = "mosaic_database.db"
        )
    }
}