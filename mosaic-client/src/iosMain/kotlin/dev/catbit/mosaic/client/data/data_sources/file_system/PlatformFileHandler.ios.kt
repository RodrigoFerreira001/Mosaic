package dev.catbit.mosaic.client.data.data_sources.file_system

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.write

actual object PlatformFileHandler {

    actual suspend fun saveFile(
        fileName: String,
        data: ByteArray
    ) {
        PlatformFile(FileKit.filesDir, fileName).write(data)
    }

    actual suspend fun getFile(
        fileName: String
    ): ByteArray? {
        val file = PlatformFile(FileKit.filesDir, fileName)
        return if (file.exists()) file.readBytes() else null
    }

    actual suspend fun deleteFile(fileName: String) {
        PlatformFile(FileKit.filesDir, fileName).delete(mustExist = false)
    }
}