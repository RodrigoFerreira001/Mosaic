package dev.catbit.mosaic.client.di

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.client.data.data_chest.IOSDataChest
import dev.catbit.mosaic.client.data.data_sources.database.MosaicRoomDatabase
import dev.catbit.mosaic.client.permission.IOSPermissionManager
import dev.catbit.mosaic.client.ui.sdui.foundation.permission.PermissionManager
import dev.catbit.mosaic.core.domain.base.IO
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDomainMask

@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
internal actual val platformModule = module {
    single<HttpClient> { HttpClient(Darwin) }

    single<PermissionManager> { IOSPermissionManager() }

    single<DataChest> {
        IOSDataChest(NSUserDefaults.standardUserDefaults())
    }

    single<MosaicRoomDatabase> {
        val applicationId: String = get(named("APPLICATION_ID"))

        val documentsDir = NSFileManager.defaultManager
            .URLsForDirectory(NSDocumentDirectory, inDomains = NSUserDomainMask)
            .firstOrNull() as? NSURL

        val dbPath = requireNotNull(documentsDir?.path) { "Could not resolve iOS Documents directory" }

        Room.databaseBuilder<MosaicRoomDatabase>(name = "$dbPath/${applicationId}_database.db")
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}
