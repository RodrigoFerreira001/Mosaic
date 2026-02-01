package dev.catbit.mosaic.client.data.data_sources.file_system

class MosaicFileSystemImpl : MosaicFileSystem {
    override suspend fun saveFile(fileName: String, data: ByteArray) {
        TODO("Not yet implemented")
    }

    override suspend fun getFile(fileName: String): ByteArray? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFile(fileName: String) {
        TODO("Not yet implemented")
    }
}