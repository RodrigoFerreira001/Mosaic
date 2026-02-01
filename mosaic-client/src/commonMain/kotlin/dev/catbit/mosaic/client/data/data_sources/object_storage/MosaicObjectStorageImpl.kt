package dev.catbit.mosaic.client.data.data_sources.object_storage

import dev.catbit.mosaic.client.data.data_chest.DataChest
import dev.catbit.mosaic.core.data.responses.graph.GraphResponse
import dev.catbit.mosaic.core.data.responses.screen.ScreenResponse
import dev.catbit.mosaic.core.serialization.MosaicSerializer

class MosaicObjectStorageImpl(
    private val dataChest: DataChest,
    private val serializer: MosaicSerializer
) : MosaicObjectStorage {

    override suspend fun setInitialGraph(initialGraph: GraphResponse) {
        dataChest.putString(
            key = INITIAL_GRAPH,
            value = serializer.encodeToString(initialGraph)
        )
    }

    override suspend fun getInitialGraph(): GraphResponse? =
        dataChest.getStringOrNull(
            key = INITIAL_GRAPH
        )?.let { serializer.decodeFromString(it) }

    override suspend fun setScreen(screenResponse: ScreenResponse) {
        dataChest.putString(
            key = screenResponse.id,
            value = serializer.encodeToString(screenResponse)
        )
    }

    override suspend fun getScreen(screenId: String): ScreenResponse? =
        dataChest.getStringOrNull(
            key = screenId
        )?.let { serializer.decodeFromString(it) }

    private companion object {
        const val INITIAL_GRAPH = "initialGraph"
    }
}