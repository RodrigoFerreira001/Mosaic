package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Lists the files directly inside a directory of the client's own private file storage (subdirectories are skipped) — backs
 * the `ListFiles` event. Fails with `DirNotFoundException` when the directory doesn't exist.
 * Reachable via `get<ListFilesUseCase>()`.
 */
class ListFilesUseCase(
    private val repository: MosaicRepository
) : UseCase<List<String>, ListFilesUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.listFiles(directory = directory)
    }

    /** @property directory directory to list, relative to the storage root. */
    data class Params(
        val directory: String
    )
}
