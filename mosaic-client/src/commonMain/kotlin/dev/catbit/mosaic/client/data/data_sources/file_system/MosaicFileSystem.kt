package dev.catbit.mosaic.client.data.data_sources.file_system

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow

interface MosaicFileSystem {
    suspend fun saveFile(
        fileName: String,
        data: ByteArray
    )
    suspend fun getFile(fileName: String): ByteArray?
    suspend fun getFilePlatformFile(fileName: String): PlatformFile?
    suspend fun deleteFile(fileName: String)
    suspend fun fileExists(fileName: String): Boolean
    suspend fun saveFileStreaming(fileName: String, data: Flow<ByteArray>)
    suspend fun getFileStreaming(fileName: String): Flow<ByteArray>?
}