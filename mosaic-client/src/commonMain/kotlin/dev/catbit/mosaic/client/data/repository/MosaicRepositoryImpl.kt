package dev.catbit.mosaic.client.data.repository

import dev.catbit.mosaic.client.data.data_sources.database.MosaicDatabase
import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystem
import dev.catbit.mosaic.client.data.data_sources.network.MosaicNetwork
import dev.catbit.mosaic.client.data.data_sources.object_storage.MosaicObjectStorage
import dev.catbit.mosaic.client.extensions.safeResult
import dev.catbit.mosaic.core.data.models.graph.GraphModel
import dev.catbit.mosaic.core.data.models.screen.ScreenModel
import dev.catbit.mosaic.core.extensions.currentDateTime
import dev.catbit.mosaic.core.extensions.toSafeLocalDateTime
import io.ktor.http.HttpMethod

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
}