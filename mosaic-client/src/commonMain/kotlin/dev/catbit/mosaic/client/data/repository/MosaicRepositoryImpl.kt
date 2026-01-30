package dev.catbit.mosaic.client.data.repository

import dev.catbit.mosaic.client.data.data_sources.MosaicNetwork
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

class MosaicRepositoryImpl(
    private val network: MosaicNetwork
) : MosaicRepository {

    override suspend fun sendHttpRequest(
        url: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod
    ): Result<HttpResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun downloadFile(
        url: String,
        headers: Map<String, String>?,
        httpMethod: HttpMethod,
        onProgress: (Int) -> Unit,
        onBytesReceived: (ByteArray) -> Unit,
        onDownloadFinished: (ByteArray) -> Unit,
        onDownloadFailure: (Throwable) -> Unit
    ): Result<Unit> {
        TODO("Not yet implemented")
    }


}