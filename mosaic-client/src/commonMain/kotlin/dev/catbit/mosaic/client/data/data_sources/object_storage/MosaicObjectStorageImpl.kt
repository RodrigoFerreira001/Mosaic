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
        runCatching {
            dataChest.getStringOrNull(
                key = INITIAL_GRAPH
            )?.let { serializer.decodeFromString<GraphResponse>(it) }
        }.getOrElse {
            dataChest.remove(INITIAL_GRAPH)
            null
        }

    override suspend fun setScreen(screenResponse: ScreenResponse) {
        dataChest.putString(
            key = screenResponse.id,
            value = serializer.encodeToString(screenResponse)
        )
    }

    override suspend fun getScreen(screenId: String): ScreenResponse? =
        runCatching {
            dataChest.getStringOrNull(
                key = screenId
            )?.let { serializer.decodeFromString<ScreenResponse>(it) }
        }.getOrElse {
            dataChest.remove(screenId)
            null
        }

    private companion object {
        const val INITIAL_GRAPH = "initialGraph"
    }
}