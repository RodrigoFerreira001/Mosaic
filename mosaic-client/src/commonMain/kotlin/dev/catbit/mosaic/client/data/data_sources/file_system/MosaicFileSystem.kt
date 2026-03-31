package dev.catbit.mosaic.client.data.data_sources.file_system

interface MosaicFileSystem {
    suspend fun saveFile(
        fileName: String,
        data: ByteArray
    )
    suspend fun getFile(fileName: String): ByteArray?
    suspend fun deleteFile(fileName: String)
}