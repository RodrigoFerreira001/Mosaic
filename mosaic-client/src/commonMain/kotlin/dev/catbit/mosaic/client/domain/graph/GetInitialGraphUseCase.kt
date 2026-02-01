package dev.catbit.mosaic.client.domain.graph

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.data.models.graph.GraphModel
import dev.catbit.mosaic.core.domain.base.UseCase

class GetInitialGraphUseCase(
    private val repository: MosaicRepository
) : UseCase<GraphModel, Unit>() {

    override suspend fun execute(params: Unit) = repository.getInitialGraph()
}