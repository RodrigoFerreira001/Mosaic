package dev.catbit.mosaic.client.domain.version

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Checks the backend's current cache version against the locally cached one, invalidating stale
 * cached screens/graph if they don't match — run once at app startup by
 * `MosaicApplicationStateHolder`, before [dev.catbit.mosaic.client.domain.graph.GetInitialGraphUseCase]
 * fetches the initial graph, so a stale cache never serves an outdated graph. Reachable via
 * `get<CheckCacheVersionUseCase>()`.
 */
class CheckCacheVersionUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, Unit>() {

    override suspend fun execute(params: Unit) = repository.getVersion()
}
