package dev.catbit.mosaic.client.data.data_sources.file_system

class MosaicFileSystemImpl : MosaicFileSystem {

    override suspend fun saveFile(
        fileName: String,
        data: ByteArray
    ) = PlatformFileHandler.saveFile(fileName, data)

    override suspend fun getFile(
        fileName: String
    ): ByteArray? = PlatformFileHandler.getFile(fileName)

    override suspend fun deleteFile(
        fileName: String
    ) = PlatformFileHandler.deleteFile(fileName)
}