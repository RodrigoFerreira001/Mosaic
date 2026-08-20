package dev.catbit.mosaic.client.domain.screen

import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.core.data.models.screen.ScreenModel
import dev.catbit.mosaic.core.domain.base.UseCase
import io.ktor.http.HttpMethod

/**
 * Fetches a screen's payload by id, without installing it — backs both `GetScreen` (which forwards
 * the [ScreenModel] downstream, letting the caller decide when/how to apply it via
 * `ChangeScreenState`) and `RefreshScreen` (which applies it immediately). Reachable via
 * `get<GetScreenUseCase>()`.
 */
class GetScreenUseCase(
    private val repository: MosaicRepository
) : UseCase<ScreenModel, GetScreenUseCase.Params>() {

    override suspend fun execute(params: Params) = with(params) {
        repository.getScreen(
            screenId = screenId,
            headers = headers,
            body = body,
            httpMethod = httpMethod,
            timeoutMillis = timeoutMillis
        )
    }

    /**
     * @property screenId id of the screen to fetch.
     * @property headers request headers.
     * @property body request body.
     * @property httpMethod HTTP method.
     * @property timeoutMillis request timeout override.
     */
    data class Params(
        val screenId: String,
        val headers: Map<String, String>?,
        val body: Any?,
        val httpMethod: HttpMethod,
        val timeoutMillis: Long? = null,
    )
}