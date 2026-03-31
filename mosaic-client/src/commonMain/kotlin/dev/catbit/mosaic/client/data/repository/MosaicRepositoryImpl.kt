package dev.catbit.mosaic.client.data.repository

import dev.catbit.mosaic.client.data.data_sources.database.MosaicDatabase
import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystem
import dev.catbit.mosaic.client.data.data_sources.network.MosaicNetwork
import dev.catbit.mosaic.client.data.data_sources.object_storage.MosaicObjectStorage
import dev.catbit.mosaic.client.exceptions.DataNotFoundException
import dev.catbit.mosaic.client.extensions.safeResult
import dev.catbit.mosaic.core.data.models.graph.GraphModel
import dev.catbit.mosaic.core.data.models.screen.ScreenModel
import dev.catbit.mosaic.core.extensions.currentDateTime
import dev.catbit.mosaic.core.extensions.toSafeLocalDateTime
import io.ktor.http.HttpMethod
import kotlinx.io.files.FileNotFoundException

class MosaicRepositoryImpl(
    private val network: MosaicNetwork,
    private val database: MosaicDatabase,
    private val objectStorage: MosaicObjectStorage,
    private val fileSystem: MosaicFileSystem
) : MosaicRepository {

    override suspend fun getInitialGraph(): Result<GraphModel> = safeResult {

        val cachedInitialGraph = objectStorage.getInitialGraph()
        val currentTime = currentDateTime()

        if (cachedInitialGraph != null) {
            val cachedInitialGraphTtl = cachedInitialGraph.ttl?.toSafeLocalDateTime()

            if (cachedInitialGraphTtl == null || cachedInitialGraphTtl > currentTime) {
                network.getInitialGraph().getOrNull()?.apply {
                    objectStorage.setInitialGraph(this)
                } ?: cachedInitialGraph
            } else cachedInitialGraph

        } else {
            network.getInitialGraph().getOrThrow().apply {
                objectStorage.setInitialGraph(this)
            }
        }
    }.map { initialGraphResponse ->
        GraphModel.fromGraphResponse(initialGraphResponse)
    }

    override suspend fun getScreen(
        screenId: String,
        headers: Map<String, String>?
    ): Result<ScreenModel> = safeResult {

        val cachedScreen = objectStorage.getScreen(screenId)
        val currentTime = currentDateTime()

        if (cachedScreen != null) {
            val cachedScreenTtl = cachedScreen.ttl?.toSafeLocalDateTime()

            if (cachedScreenTtl == null || cachedScreenTtl > currentTime) {
                network.getScreen(
                    screenId = screenId,
                    headers = headers,
                ).getOrNull()?.apply {
                    objectStorage.setScreen(this)
                } ?: cachedScreen
            } else cachedScreen
        } else {
            network.getScreen(
                screenId = screenId,
                headers = headers,
            ).getOrThrow().apply {
                objectStorage.setScreen(this)
            }
        }
    }.map { screenResponse ->
        ScreenModel.fromScreenResponse(screenResponse)
    }

    override suspend fun sendHttpRequest(
        url: String,
        headers: Map<String, String>?,
        body: Any?,
        httpMethod: HttpMethod
    ) = network.sendHttpRequest(
        url = url,
        headers = headers,
        body = body,
        httpMethod = httpMethod,
    )

    override suspend fun downloadFile(
        url: String,
        headers: Map<String, String>?,
        httpMethod: HttpMethod,
        onProgress: (Int) -> Unit,
        onBytesReceived: (ByteArray) -> Unit,
        onDownloadFinished: (ByteArray) -> Unit,
        onDownloadFailure: (Throwable) -> Unit
    ) = network.downloadFile(
        url = url,
        headers = headers,
        httpMethod = httpMethod,
        onProgress = onProgress,
        onBytesReceived = onBytesReceived,
        onDownloadFinished = onDownloadFinished,
        onDownloadFailure = onDownloadFailure,
    )

    override suspend fun getPlainData(
        dataKey: String
    ): Result<Any> = safeResult {
        database.getPlainData(dataKey) ?: throw DataNotFoundException(dataKey)
    }

    override suspend fun getAllPlainData(): Result<Map<String, Any>> = safeResult{
        database.getAllPlainData() ?: throw DataNotFoundException("All PlainData")
    }

    override suspend fun savePlainData(
        dataKey: String,
        data: Any
    ): Result<Unit> = safeResult {
        database.setPlainData(
            dataKey = dataKey,
            data = data
        )
    }

    override suspend fun getPlainDataByIds(
        dataKeys: List<String>
    ): Result<Map<String, Any>> = safeResult {
        database.getPlainDataByIds(dataKeys) ?: throw DataNotFoundException("PlainData for ids $dataKeys")
    }

    override suspend fun deletePlainData(
        dataKey: String
    ): Result<Unit> = safeResult {
        database.deletePlainData(dataKey)
    }

    override suspend fun deletePlainDataByIds(
        dataKeys: List<String>
    ): Result<Unit> = safeResult {
        database.deletePlainDataByIds(dataKeys)
    }

    override suspend fun wipePlainData(): Result<Unit> = safeResult{
        database.wipePlainData()
    }

    override suspend fun getSegmentedData(
        segmentKey: String,
        dataKey: String
    ): Result<Any> = safeResult {
        database.getSegmentedData(
            segmentKey = segmentKey,
            dataKey = dataKey
        ) ?: throw DataNotFoundException("$segmentKey $dataKey")
    }

    override suspend fun getAllSegmentedData(
        segmentKey: String
    ): Result<Map<String, Any>> = safeResult{
        database.getAllSegmentedData(segmentKey) ?: throw DataNotFoundException("All SegmentedData for $segmentKey")
    }

    override suspend fun saveSegmentedData(
        segmentKey: String,
        dataKey: String,
        data: Any
    ): Result<Unit> = safeResult {
        database.setSegmentedData(
            segmentKey = segmentKey,
            dataKey = dataKey,
            data = data
        )
    }

    override suspend fun getSegmentedDataByIds(
        segmentKey: String,
        dataKeys: List<String>
    ): Result<Map<String, Any>> = safeResult {
        database.getSegmentedDataByIds(segmentKey, dataKeys) ?: throw DataNotFoundException("SegmentedData for segment '$segmentKey' and ids $dataKeys")
    }

    override suspend fun deleteSegmentedData(
        segmentKey: String,
        dataKey: String
    ): Result<Unit> = safeResult {
        database.deleteSegmentedData(
            segmentKey = segmentKey,
            dataKey = dataKey
        )
    }

    override suspend fun deleteSegmentedDataByIds(
        segmentKey: String,
        dataKeys: List<String>
    ): Result<Unit> = safeResult {
        database.deleteSegmentedDataByIds(segmentKey, dataKeys)
    }

    override suspend fun wipeSegmentedData(
        segmentKey: String
    ): Result<Unit> = safeResult{
        database.wipeSegmentedData(segmentKey)
    }

    override suspend fun saveFile(
        fileName: String,
        data: ByteArray
    ): Result<Unit> = safeResult {
        fileSystem.saveFile(
            fileName = fileName,
            data = data
        )
    }

    override suspend fun getFile(
        fileName: String
    ): Result<ByteArray> = safeResult {
        fileSystem.getFile(fileName) ?: throw FileNotFoundException(fileName)
    }

    override suspend fun deleteFile(
        fileName: String
    ): Result<Unit> = safeResult {
        fileSystem.deleteFile(fileName)
    }
}