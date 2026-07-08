package dev.catbit.mosaic.client.data.data_sources.file_system

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow

class MosaicFileSystemImpl : MosaicFileSystem {

    override suspend fun saveFile(
        fileName: String,
        data: ByteArray
    ) = PlatformFileHandler.saveFile(fileName, data)

    override suspend fun getFile(
        fileName: String
    ): ByteArray? = PlatformFileHandler.getFile(fileName)

    override suspend fun getFilePlatformFile(
        fileName: String
    ): PlatformFile? = PlatformFileHandler.getFilePlatformFile(fileName)

    override suspend fun deleteFile(
        fileName: String
    ) = PlatformFileHandler.deleteFile(fileName)

    override suspend fun fileExists(fileName: String): Boolean =
        PlatformFileHandler.fileExists(fileName)

    override suspend fun saveFileStreaming(fileName: String, data: Flow<ByteArray>) =
        PlatformFileHandler.saveFileStreaming(fileName, data)

    override suspend fun getFileStreaming(fileName: String): Flow<ByteArray>? =
        PlatformFileHandler.getFileStreaming(fileName)
}