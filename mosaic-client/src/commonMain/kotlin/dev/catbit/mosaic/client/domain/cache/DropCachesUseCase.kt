package dev.catbit.mosaic.client.domain.cache

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

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

    data class Params(
        val dropScreensCache: Boolean,
        val dropInitialGraphCache: Boolean,
        val dropVersionCache: Boolean
    )
}
