package dev.catbit.mosaic.client.domain.file

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.domain.base.UseCase

/**
 * Deletes files from the client's own private file storage — backs the `DeleteFiles` event.
 * Every file is attempted even after one fails; if any failed, the result is a failure carrying the
 * first `Throwable`, with the others attached as suppressed. Reachable via `get<DeleteFilesUseCase>()`.
 */
class DeleteFilesUseCase(
    private val repository: MosaicRepository
) : UseCase<Unit, DeleteFilesUseCase.Params>() {

    override suspend fun execute(params: Params): Result<Unit> {
        val failures = params.fileNames.mapNotNull { fileName ->
            repository.deleteFile(fileName = fileName).exceptionOrNull()
        }
        val first = failures.firstOrNull() ?: return Result.success(Unit)
        failures.drop(1).forEach { first.addSuppressed(it) }
        return Result.failure(first)
    }

    /** @property fileNames files to delete. */
    data class Params(
        val fileNames: List<String>
    )
}
