package dev.catbit.mosaic.client.domain.graph

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.data.models.graph.GraphModel
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Fetches the app's initial navigation graph — the `Graph`/`Screen` payload `MosaicApplicationStateHolder`
 * loads once at app startup, before the first screen can render. Reachable via
 * `get<GetInitialGraphUseCase>()`.
 */
class GetInitialGraphUseCase(
    private val repository: MosaicRepository
) : UseCase<GraphModel, Unit>() {

    override suspend fun execute(params: Unit) = repository.getInitialGraph()
}