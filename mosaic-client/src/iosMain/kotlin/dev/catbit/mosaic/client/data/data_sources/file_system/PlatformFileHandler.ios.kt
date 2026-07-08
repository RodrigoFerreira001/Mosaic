package dev.catbit.mosaic.client.data.data_sources.file_system

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.parent
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.sink
import io.github.vinceglb.filekit.source
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.buffered

actual object PlatformFileHandler {

    private const val STREAM_CHUNK_SIZE = 64 * 1024

    private suspend fun ensureParentExists(file: PlatformFile) {
        file.parent()?.takeIf { !it.exists() }?.createDirectories()
    }

    actual suspend fun saveFile(
        fileName: String,
        data: ByteArray
    ) {
        val file = PlatformFile(FileKit.filesDir, fileName)
        ensureParentExists(file)
        file.write(data)
    }

    actual suspend fun getFile(
        fileName: String
    ): ByteArray? {
        val file = PlatformFile(FileKit.filesDir, fileName)
        return if (file.exists()) file.readBytes() else null
    }

    actual suspend fun getFilePlatformFile(fileName: String): PlatformFile? {
        val file = PlatformFile(FileKit.filesDir, fileName)
        return file.takeIf { it.exists() }
    }

    actual suspend fun deleteFile(fileName: String) {
        PlatformFile(FileKit.filesDir, fileName).delete(mustExist = false)
    }

    actual suspend fun fileExists(fileName: String): Boolean =
        PlatformFile(FileKit.filesDir, fileName).exists()

    actual suspend fun saveFileStreaming(fileName: String, data: Flow<ByteArray>) {
        val file = PlatformFile(FileKit.filesDir, fileName)
        ensureParentExists(file)
        file.sink().buffered().use { sink ->
            data.collect { chunk -> sink.write(chunk) }
        }
    }

    actual suspend fun getFileStreaming(fileName: String): Flow<ByteArray>? {
        val file = PlatformFile(FileKit.filesDir, fileName)
        if (!file.exists()) return null
        return flow {
            file.source().buffered().use { src ->
                val buffer = ByteArray(STREAM_CHUNK_SIZE)
                while (!src.exhausted()) {
                    val read = src.readAtMostTo(buffer)
                    if (read > 0) emit(buffer.copyOfRange(0, read))
                }
            }
        }
    }
}