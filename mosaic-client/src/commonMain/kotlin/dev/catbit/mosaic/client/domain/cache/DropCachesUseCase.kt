package dev.catbit.mosaic.client.domain.cache

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Clears one or more of the client's caches — the mechanism behind the `DropCaches` event.
 * Reachable via `get<DropCachesUseCase>()` from a custom `EventRunner`/`TileHolderBuilder` that
 * needs the same cache-clearing behavior.
 */
class DropCachesUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, DropCachesUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.dropCaches(
            dropScreensCache = dropScreensCache,
            dropInitialGraphCache = dropInitialGraphCache,
            dropVersionCache = dropVersionCache
        )
    }

    /**
     * @property dropScreensCache whether to clear cached screen payloads.
     * @property dropInitialGraphCache whether to clear the cached initial navigation graph.
     * @property dropVersionCache whether to clear the cached cache-version marker.
     */
    data class Params(
        val dropScreensCache: Boolean,
        val dropInitialGraphCache: Boolean,
        val dropVersionCache: Boolean
    )
}
