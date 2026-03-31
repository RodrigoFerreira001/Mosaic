package dev.catbit.mosaic.client.data.repository

import dev.catbit.mosaic.core.data.models.graph.GraphModel
import dev.catbit.mosaic.core.data.models.screen.ScreenModel
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

interface MosaicRepository {

    suspend fun getInitialGraph(): Result<GraphModel>

    suspend fun getScreen(
        screenId: String,
        headers: Map<String, String>?
    ): Result<ScreenModel>

    suspend fun sendHttpRequest(
        url: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod
    ): Result<HttpResponse>

    suspend fun downloadFile(
        url: String,
        headers: Map<String, String>?,
        httpMethod: HttpMethod,
        onProgress: (Int) -> Unit = {},
        onBytesReceived: (ByteArray) -> Unit = {},
        onDownloadFinished: (ByteArray) -> Unit = {},
        onDownloadFailure: (Throwable) -> Unit = {}
    ): Result<Unit>

    suspend fun getPlainData(
        dataKey: String
    ): Result<Any>

    suspend fun getAllPlainData(): Result<Map<String,Any>>

    suspend fun savePlainData(
        dataKey: String,
        data: Any
    ): Result<Unit>

    suspend fun getPlainDataByIds(
        dataKeys: List<String>
    ): Result<Map<String, Any>>

    suspend fun deletePlainData(
        dataKey: String,
    ): Result<Unit>

    suspend fun deletePlainDataByIds(
        dataKeys: List<String>
    ): Result<Unit>

    suspend fun wipePlainData(): Result<Unit>

    suspend fun getSegmentedData(
        segmentKey: String,
        dataKey: String
    ): Result<Any>

    suspend fun getAllSegmentedData(
        segmentKey: String
    ): Result<Map<String, Any>>

    suspend fun saveSegmentedData(
        segmentKey: String,
        dataKey: String,
        data: Any
    ): Result<Unit>

    suspend fun getSegmentedDataByIds(
        segmentKey: String,
        dataKeys: List<String>
    ): Result<Map<String, Any>>

    suspend fun deleteSegmentedData(
        segmentKey: String,
        dataKey: String
    ): Result<Unit>

    suspend fun deleteSegmentedDataByIds(
        segmentKey: String,
        dataKeys: List<String>
    ): Result<Unit>

    suspend fun wipeSegmentedData(
        segmentKey: String
    ): Result<Unit>

    suspend fun saveFile(
        fileName: String,
        data: ByteArray
    ): Result<Unit>

    suspend fun getFile(
        fileName: String
    ): Result<ByteArray>

    suspend fun deleteFile(
        fileName: String
    ): Result<Unit>
}