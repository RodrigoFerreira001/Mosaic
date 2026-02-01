package dev.catbit.mosaic.client.data.data_sources.object_storage

import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse

interface MosaicObjectStorage {
    //https://github.com/xxfast/KStore
    suspend fun setInitialGraph(initialGraph: GraphResponse)
    suspend fun getInitialGraph(): GraphResponse?
    suspend fun setScreen(screenResponse: ScreenResponse)
    suspend fun getScreen(screenId: String): ScreenResponse?
}