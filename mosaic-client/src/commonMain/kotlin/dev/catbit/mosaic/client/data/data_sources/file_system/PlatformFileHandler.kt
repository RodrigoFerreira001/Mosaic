package dev.catbit.mosaic.client.data.data_sources.file_system

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow

expect object PlatformFileHandler {

    suspend fun saveFile(
        fileName: String,
        data: ByteArray
    )
    suspend fun getFile(fileName: String): ByteArray?

    /** Returns a [PlatformFile] reference to the file, or null if it doesn't exist. */
    suspend fun getFilePlatformFile(fileName: String): PlatformFile?
    suspend fun deleteFile(fileName: String)
    suspend fun fileExists(fileName: String): Boolean

    /** Writes [data] to disk incrementally, without ever holding the full file in memory. */
    suspend fun saveFileStreaming(fileName: String, data: Flow<ByteArray>)

    /** Returns a [Flow] that reads the file in chunks, or null if the file doesn't exist. */
    suspend fun getFileStreaming(fileName: String): Flow<ByteArray>?
}