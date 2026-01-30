package dev.catbit.mosaic.client.data.data_sources

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

interface MosaicNetwork {

    suspend fun sendHttpRequest(
        url: String,
        headers: Map<String, String>? = null,
        body: Any?,
        httpMethod: HttpMethod
    ): HttpResponse

    suspend fun downloadFile(
        url: String,
        headers: Map<String, String>? = null,
        httpMethod: HttpMethod,
        onProgress: (Int) -> Unit = {},
        onBytesReceived: (ByteArray) -> Unit = {},
        onDownloadFinished: (ByteArray) -> Unit = {},
        onDownloadFailure: (Throwable) -> Unit = {}
    )
}