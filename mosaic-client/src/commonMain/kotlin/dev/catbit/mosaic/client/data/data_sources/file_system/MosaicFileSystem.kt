package dev.catbit.mosaic.client.data.data_sources.file_system

interface MosaicFileSystem {
    //https://github.com/vinceglb/FileKit
    suspend fun saveFile(
        fileName: String,
        data: ByteArray
    )
    suspend fun getFile(fileName: String): ByteArray?
    suspend fun deleteFile(fileName: String)
}